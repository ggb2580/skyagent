<template>
  <div class="app">
    <!-- 顶部导航栏（毛玻璃效果 + 极简风格） -->
    <header class="navbar">
      <div class="navbar-brand">
        <span class="brand-logo">✈️</span>
        <div class="brand-text">
          <span class="brand-name">智航AI</span>
          <span class="brand-sub">智能机票预订 · AI助手</span>
        </div>
      </div>
      <nav class="nav-links">
        <a href="#" :class="{ active: activeNav === 'home' }" @click.prevent="activeNav = 'home'">首页</a>
        <a href="#" :class="{ active: activeNav === 'search' }" @click.prevent="activeNav = 'search'">机票查询</a>
        <a href="#" :class="{ active: activeNav === 'orders' }" @click.prevent="activeNav = 'orders'">我的订单</a>
        <a href="#" :class="{ active: activeNav === 'profile' }" @click.prevent="activeNav = 'profile'">个人中心</a>
        <a href="#" :class="{ active: activeNav === 'assistant' }" @click.prevent="activeNav = 'assistant'">AI助手</a>
      </nav>
    </header>

    <main class="main-container">
      <!-- 左侧主内容区 -->
      <div class="main-content">
        <!-- 搜索卡片（携程风格紧凑表单） -->
        <div class="search-card">
          <div class="trip-type">
            <el-radio-group v-model="queryForm.tripType" size="large">
              <el-radio label="oneway">单程</el-radio>
              <el-radio label="roundtrip">往返</el-radio>
            </el-radio-group>
          </div>
          <div class="search-fields">
            <div class="field">
              <span class="field-icon">🛫</span>
              <el-input v-model="queryForm.fromCity" placeholder="出发城市" clearable />
            </div>
            <div class="field-exchange">⇄</div>
            <div class="field">
              <span class="field-icon">🛬</span>
              <el-input v-model="queryForm.toCity" placeholder="到达城市" clearable />
            </div>
            <div class="field">
              <el-date-picker
                v-model="queryForm.departDate"
                type="date"
                placeholder="出发日期"
                :disabled-date="disabledPastDate"
              />
            </div>
            <div class="field" v-if="queryForm.tripType === 'roundtrip'">
              <el-date-picker
                v-model="queryForm.returnDate"
                type="date"
                placeholder="返程日期"
                :disabled-date="disabledPastDate"
              />
            </div>
            <div class="field">
              <el-select v-model="queryForm.cabinClass" placeholder="舱位等级">
                <el-option label="经济舱" value="economy" />
                <el-option label="商务舱" value="business" />
                <el-option label="头等舱" value="first" />
              </el-select>
            </div>
            <el-button type="primary" class="search-btn" @click="handleQueryFlights" :loading="loading">
              查询航班
            </el-button>
          </div>
        </div>

        <!-- 航班结果列表（携程风格卡片） -->
        <div class="flights-section">
          <div class="section-header">
            <h3>航班结果</h3>
            <span v-if="flights.length > 0" class="result-count">{{ flights.length }}个航班</span>
          </div>
          <div v-if="flights.length === 0" class="empty-flights">
            <div class="empty-icon">✈️</div>
            <p>输入出发地、目的地和日期，查看航班</p>
          </div>
          <div v-else class="flight-list">
            <div class="flight-item" v-for="flight in flights" :key="flight.id">
              <div class="flight-info">
                <div class="airline">
                  <span class="airline-name">{{ flight.airline }}</span>
                  <span class="flight-no">{{ flight.flightNo }}</span>
                </div>
                <div class="route-time">
                  <div class="time-point">
                    <span class="time">{{ flight.departTime }}</span>
                    <span class="city">{{ flight.from }}</span>
                  </div>
                  <div class="duration-line">
                    <span>———</span>
                    <span>✈️</span>
                    <span>———</span>
                  </div>
                  <div class="time-point">
                    <span class="time">{{ flight.arriveTime }}</span>
                    <span class="city">{{ flight.to }}</span>
                  </div>
                </div>
                <div class="cabin">{{ flight.cabin }}</div>
              </div>
              <div class="flight-price">
                <span class="price">¥{{ flight.price }}</span>
                <el-button type="primary" text size="small" @click="bookFlight(flight)">预订</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧辅助区（AI助手 + 订单） -->
      <aside class="sidebar">
        <!-- AI助手卡片（苹果风格聊天窗） -->
        <div class="assistant-card">
          <div class="card-header">
            <span class="header-icon">🤖</span>
            <span class="header-title">AI 智能助手</span>
          </div>
          <!-- 聊天窗口容器：自定义滚动条（默认隐藏，hover显示） -->
          <div class="chat-window" ref="chatWindow">
            <div v-for="(item, idx) in chatHistory" :key="idx" :class="['message', item.role]">
              <div class="avatar" :class="item.role">
                {{ item.role === 'assistant' ? 'AI' : '我' }}
              </div>
              <div class="bubble">{{ item.content }}</div>
            </div>
            <div ref="chatEnd"></div>
          </div>
          <el-input
            type="textarea"
            :rows="3"
            v-model="userQuestion"
            placeholder="问一问航班推荐、特价机票… (按回车发送)"
            class="chat-input"
            @keydown.enter.prevent="sendChat"
          />
          <div class="chat-actions">
            <el-button type="primary" @click="sendChat" :loading="chatLoading">发送消息</el-button>
          </div>
        </div>

        <!-- 我的订单卡片 -->
        <div class="order-card">
          <div class="card-header">
            <span class="header-icon">📋</span>
            <span class="header-title">我的订单</span>
            <el-button link type="primary" size="small">查看全部</el-button>
          </div>
          <div class="order-list">
            <div v-for="order in orders" :key="order.orderNo" class="order-item">
              <div class="order-route">{{ order.route }}</div>
              <div class="order-meta">
                <span class="order-no">{{ order.orderNo }}</span>
                <span class="order-status">{{ order.status }}</span>
              </div>
            </div>
            <div v-if="orders.length === 0" class="empty-orders">暂无订单</div>
          </div>
        </div>
      </aside>
    </main>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { queryFlights as queryFlightsApi, getOrders } from './services/api'

// 响应式数据
const activeNav = ref('search')
const queryForm = reactive({
  tripType: 'oneway',
  fromCity: '',
  toCity: '',
  departDate: '',
  returnDate: '',
  cabinClass: 'economy'
})
const flights = ref([])
const orders = ref([])
const chatHistory = ref([])
const userQuestion = ref('')
const loading = ref(false)
const chatLoading = ref(false)
const chatWindow = ref(null)
const chatEnd = ref(null)

// 日期限制（禁止过去日期）
const disabledPastDate = (time) => time.getTime() < Date.now() - 86400000

// 查询航班
const handleQueryFlights = async () => {
  if (!queryForm.fromCity || !queryForm.toCity || !queryForm.departDate) {
    ElMessage.warning('请填写出发城市、到达城市和出发日期')
    return
  }
  if (queryForm.tripType === 'roundtrip' && !queryForm.returnDate) {
    ElMessage.warning('请填写返程日期')
    return
  }
  loading.value = true
  try {
    const res = await queryFlightsApi({
      fromCity: queryForm.fromCity,
      toCity: queryForm.toCity,
      departDate: queryForm.departDate,
      returnDate: queryForm.tripType === 'roundtrip' ? queryForm.returnDate : '',
      cabinClass: queryForm.cabinClass,
      tripType: queryForm.tripType
    })
    flights.value = res.data
  } catch {
    ElMessage.error('航班查询失败')
  } finally {
    loading.value = false
  }
}

// 加载订单
const loadOrders = async () => {
  try {
    const res = await getOrders()
    orders.value = res.data
  } catch {
    orders.value = []
  }
}

// 聊天滚动到底部
const scrollToChatBottom = () => {
  nextTick(() => {
    if (chatWindow.value) chatWindow.value.scrollTop = chatWindow.value.scrollHeight
  })
}

const sendChat = async () => {
  const question = userQuestion.value.trim()
  if (!question) {
    ElMessage.warning('请输入问题')
    return
  }

  chatHistory.value.push({ role: 'user', content: question })
  userQuestion.value = ''
  scrollToChatBottom()

  const aiMessageIndex = chatHistory.value.length
  chatHistory.value.push({ role: 'assistant', content: '' })
  scrollToChatBottom()

  chatLoading.value = true

  try {
    const response = await fetch('/api/aichat', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',  // 改为 JSON
      },
      body: JSON.stringify({
        id: 1,
        message: question,
      }),
    })

    if (!response.ok) throw new Error(`HTTP ${response.status}`)

    const reader = response.body.getReader()
    const decoder = new TextDecoder('utf-8')
    let accumulated = ''

    while (true) {
      const { done, value } = await reader.read()
      if (done) break
      accumulated += decoder.decode(value, { stream: true })
      chatHistory.value[aiMessageIndex].content = accumulated
      scrollToChatBottom()
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('AI 助手响应失败')
    chatHistory.value[aiMessageIndex].content = '抱歉，我遇到了一些问题，请稍后再试。'
    scrollToChatBottom()
  } finally {
    chatLoading.value = false
  }
}

// 预订航班
const bookFlight = (flight) => {
  ElMessage.success(`已预订 ${flight.airline} ${flight.flightNo} · ¥${flight.price}`)
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
/* ---------- 全局 Apple 风格重置 ---------- */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, 'SF Pro Text', 'Helvetica Neue', sans-serif;
  background-color: #f5f5f7;
}

.app {
  min-height: 100vh;
  background: #f5f5f7;
}

/* 顶部导航栏（毛玻璃 + 极简） */
.navbar {
  position: sticky;
  top: 0;
  z-index: 10;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 32px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-bottom: 0.5px solid rgba(0, 0, 0, 0.05);
}

.navbar-brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.brand-logo {
  font-size: 28px;
}

.brand-text {
  display: flex;
  flex-direction: column;
}

.brand-name {
  font-size: 20px;
  font-weight: 590;
  letter-spacing: -0.3px;
  color: #1c1c1e;
}

.brand-sub {
  font-size: 11px;
  color: #6c6c70;
}

.nav-links {
  display: flex;
  gap: 28px;
}

.nav-links a {
  text-decoration: none;
  font-size: 15px;
  font-weight: 480;
  color: #1c1c1e;
  transition: color 0.2s;
}

.nav-links a.active {
  color: #007aff;
  font-weight: 590;
}

/* 主容器布局（携程式左右结构） */
.main-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 32px 24px;
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 32px;
}

/* 左侧主内容区 */
.main-content {
  display: flex;
  flex-direction: column;
  gap: 28px;
}

/* 搜索卡片（携程紧凑表单） */
.search-card {
  background: #ffffff;
  border-radius: 28px;
  padding: 24px 28px;
  box-shadow: 0 8px 28px rgba(0, 0, 0, 0.04), 0 0 0 0.5px rgba(0, 0, 0, 0.02);
}

.trip-type {
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f2;
}

.search-fields {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 16px;
}

.field {
  flex: 1 1 180px;
  display: flex;
  align-items: center;
  gap: 8px;
  background: #f8f8fc;
  border-radius: 18px;
  padding: 6px 16px;
  transition: all 0.2s;
}

.field-icon {
  font-size: 18px;
  opacity: 0.7;
}

.field-exchange {
  font-size: 20px;
  color: #8e8e93;
  font-weight: 500;
  padding: 0 4px;
}

:deep(.el-input__wrapper) {
  background: transparent !important;
  box-shadow: none !important;
  padding: 0;
}

:deep(.el-input__inner) {
  height: 44px;
  font-size: 15px;
}

.search-btn {
  background: #007aff;
  border: none;
  border-radius: 40px;
  padding: 12px 28px;
  font-weight: 590;
  font-size: 15px;
  transition: 0.2s;
}

.search-btn:hover {
  background: #005fc1;
  transform: scale(0.98);
}

/* 航班结果区域 */
.flights-section {
  background: transparent;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 20px;
  padding: 0 4px;
}

.section-header h3 {
  font-size: 20px;
  font-weight: 590;
  color: #1c1c1e;
}

.result-count {
  font-size: 13px;
  color: #8e8e93;
}

.empty-flights {
  background: #ffffff;
  border-radius: 28px;
  padding: 60px 20px;
  text-align: center;
  color: #8e8e93;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.02);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.flight-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 携程风格航班卡片 */
.flight-item {
  background: #ffffff;
  border-radius: 24px;
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.02), 0 0 0 0.5px rgba(0, 0, 0, 0.02);
  transition: all 0.2s;
}

.flight-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.08);
}

.flight-info {
  flex: 2;
}

.airline {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 12px;
}

.airline-name {
  font-weight: 590;
  font-size: 16px;
}

.flight-no {
  font-size: 13px;
  color: #6c6c70;
}

.route-time {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

.time-point {
  display: flex;
  flex-direction: column;
}

.time {
  font-size: 18px;
  font-weight: 560;
  color: #1c1c1e;
}

.city {
  font-size: 13px;
  color: #6c6c70;
}

.duration-line {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #c6c6c8;
  font-size: 12px;
}

.cabin {
  margin-top: 10px;
  font-size: 13px;
  color: #007aff;
  background: #e9f0fe;
  display: inline-block;
  padding: 4px 12px;
  border-radius: 30px;
}

.flight-price {
  text-align: right;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}

.price {
  font-size: 24px;
  font-weight: 700;
  color: #1c1c1e;
}

/* 右侧边栏 */
.sidebar {
  display: flex;
  flex-direction: column;
  gap: 28px;
}

/* AI 助手卡片（苹果风聊天窗） */
.assistant-card {
  background: #ffffff;
  border-radius: 32px;
  padding: 24px;
  box-shadow: 0 8px 28px rgba(0, 0, 0, 0.04);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.header-icon {
  font-size: 24px;
}

.header-title {
  font-size: 18px;
  font-weight: 590;
  flex: 1;
}

/* 聊天窗口 - 苹果风格滚动条（默认隐藏，悬停显示） */
.chat-window {
  height: 280px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 20px;
  padding-right: 8px;
  scrollbar-width: thin;          /* Firefox：窄滚动条，但默认仍可见 */
  scrollbar-color: transparent transparent; /* Firefox 默认透明 */
  transition: scrollbar-color 0.2s;
}

/* WebKit 浏览器（Chrome/Safari）滚动条样式 */
.chat-window::-webkit-scrollbar {
  width: 6px;
  background-color: transparent;
  transition: background-color 0.2s;
}

.chat-window::-webkit-scrollbar-track {
  background-color: transparent;
  border-radius: 10px;
}

.chat-window::-webkit-scrollbar-thumb {
  background-color: transparent;
  border-radius: 10px;
  transition: background-color 0.2s;
}

/* 鼠标悬停时显示滚动条 */
.chat-window:hover::-webkit-scrollbar-thumb {
  background-color: rgba(110, 110, 120, 0.4); /* 半透明灰色，苹果风格 */
}

.chat-window:hover::-webkit-scrollbar-track {
  background-color: rgba(0, 0, 0, 0.02);
}

/* Firefox 悬停时显示滚动条颜色 */
.chat-window:hover {
  scrollbar-color: rgba(110, 110, 120, 0.4) transparent;
}

/* 消息气泡布局 */
.message {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.message.assistant {
  flex-direction: row;
}

.message.user {
  flex-direction: row-reverse;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 500;
  flex-shrink: 0;
}

.avatar.assistant {
  background: #e9f0fe;
  color: #007aff;
}

.avatar.user {
  background: #007aff;
  color: white;
}

.bubble {
  max-width: 75%;
  padding: 12px 18px;
  border-radius: 24px;
  font-size: 14px;
  line-height: 1.45;
  word-break: break-word;
}

.message.assistant .bubble {
  background: #f2f2f7;
  color: #1c1c1e;
  border-radius: 24px 24px 24px 8px;
}

.message.user .bubble {
  background: #007aff;
  color: white;
  border-radius: 24px 24px 8px 24px;
}

.chat-input :deep(.el-textarea__inner) {
  border-radius: 22px;
  background: #f8f8fc;
  border: none;
  padding: 14px 18px;
  font-size: 14px;
  resize: none;
}

.chat-actions {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

/* 订单卡片 */
.order-card {
  background: #ffffff;
  border-radius: 32px;
  padding: 24px;
  box-shadow: 0 8px 28px rgba(0, 0, 0, 0.04);
}

.order-card .card-header {
  margin-bottom: 16px;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.order-item {
  padding: 14px 0;
  border-bottom: 0.5px solid #e9e9ef;
}

.order-route {
  font-weight: 500;
  margin-bottom: 6px;
}

.order-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #8e8e93;
}

.order-status {
  color: #007aff;
  font-weight: 500;
}

.empty-orders {
  text-align: center;
  color: #8e8e93;
  padding: 20px 0;
}

/* 响应式 */
@media (max-width: 900px) {
  .main-container {
    grid-template-columns: 1fr;
    padding: 20px;
  }
  .navbar {
    flex-direction: column;
    gap: 12px;
    padding: 16px;
  }
  .search-fields {
    flex-direction: column;
  }
  .field-exchange {
    display: none;
  }
}
</style>