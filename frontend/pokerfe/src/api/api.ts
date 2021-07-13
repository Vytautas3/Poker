import http from './http-common'

class PokerService {
  getInfo () {
    return http.get('/poker/info')
  };

  results (data: any) {
    return http.post('/poker/results', data)
  };
}

export default new PokerService()
