import axios from 'axios'

const apiClient = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
})

const mockFlightList = (params) => {
  const airlines = ['智航航空', '蓝翼航空', '云旅航空', '天际航空']
  const cabins = { economy: '经济舱', business: '商务舱', first: '头等舱' }
  const basePrice = { economy: 680, business: 1480, first: 2980 }
  return Array.from({ length: 5 }, (_, i) => {
    const idx = i + 1
    const departHour = 8 + idx * 2
    return {
      id: `${params.fromCity}-${params.toCity}-${idx}`,
      airline: airlines[idx % airlines.length],
      flightNo: `ZH${420 + idx}`,
      from: params.fromCity,
      to: params.toCity,
      departTime: `${String(departHour).padStart(2, '0')}:20`,
      arriveTime: `${String(departHour + 2).padStart(2, '0')}:05`,
      cabin: cabins[params.cabinClass] || '经济舱',
      price: basePrice[params.cabinClass] + idx * 120,
    }
  })
}

const mockOrders = () => [
  { orderNo: 'ZH20260415001', route: '北京 → 上海', status: '已出票' },
  { orderNo: 'ZH20260415002', route: '广州 → 深圳', status: '待支付' },
]

async function request(config) {
  return new Promise((resolve) => {
    setTimeout(() => {
      if (config.url === '/api/flight/query') {
        resolve({ data: mockFlightList(config.params || config.data || {}) })
      } else if (config.url === '/api/flight/orders') {
        resolve({ data: mockOrders() })
      } else {
        resolve({ data: {} })
      }
    }, 800)
  })
}

export function queryFlights(params) {
  return request({ url: '/api/flight/query', method: 'get', params })
}

export function getOrders() {
  return request({ url: '/api/flight/orders', method: 'get' })
}

