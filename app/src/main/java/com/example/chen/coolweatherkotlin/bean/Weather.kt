package com.example.chen.coolweatherkotlin.bean

/**
 * Coder : chenshuaiyu
 * Time : 2019/2/9 16:14
 */

class Weather {
    var heWeather: List<HeWeatherBean>? = null

    class HeWeatherBean {
        /**
         * basic : {"cid":"CN101100704","location":"隰县","parent_city":"临汾","admin_area":"山西","cnty":"中国","lat":"36.69267654","lon":"110.93580627","tz":"+8.00","city":"隰县","id":"CN101100704","update":{"loc":"2019-02-18 14:58","utc":"2019-02-18 06:58"}}
         * update : {"loc":"2019-02-18 14:58","utc":"2019-02-18 06:58"}
         * status : ok
         * now : {"cloud":"99","cond_code":"104","cond_txt":"阴","fl":"-2","hum":"70","pcpn":"0.0","pres":"1023","tmp":"0","vis":"14","wind_deg":"255","wind_dir":"西南风","wind_sc":"1","wind_spd":"2","cond":{"code":"104","txt":"阴"}}
         * daily_forecast : [{"date":"2019-02-18","cond":{"txt_d":"小雪"},"tmp":{"max":"-1","min":"-9"}},{"date":"2019-02-19","cond":{"txt_d":"晴"},"tmp":{"max":"6","min":"-6"}},{"date":"2019-02-20","cond":{"txt_d":"阴"},"tmp":{"max":"8","min":"-3"}}]
         * aqi : {"city":{"aqi":"109","pm25":"82","qlty":"轻度污染"}}
         * suggestion : {"comf":{"type":"comf","brf":"较不舒适","txt":"白天会有降雪发生，您会感觉偏冷，不很舒适，请注意添加衣物。"},"sport":{"type":"sport","brf":"较不宜","txt":"有降雪，推荐您在室内进行低强度运动；若坚持户外运动，请选择合适运动并注意保暖。"},"cw":{"type":"cw","brf":"不宜","txt":"不宜洗车，未来24小时内有雪，如果在此期间洗车，雪水和路上的泥水可能会再次弄脏您的爱车。"}}
         */

        var basic: BasicBean? = null
        var update: UpdateBeanX? = null
        var status: String? = null
        var now: NowBean? = null
        var aqi: AqiBean? = null
        var suggestion: SuggestionBean? = null
        var daily_forecast: List<DailyForecastBean>? = null

        class BasicBean {
            /**
             * cid : CN101100704
             * location : 隰县
             * parent_city : 临汾
             * admin_area : 山西
             * cnty : 中国
             * lat : 36.69267654
             * lon : 110.93580627
             * tz : +8.00
             * city : 隰县
             * id : CN101100704
             * update : {"loc":"2019-02-18 14:58","utc":"2019-02-18 06:58"}
             */

            var cid: String? = null
            var location: String? = null
            var parent_city: String? = null
            var admin_area: String? = null
            var cnty: String? = null
            var lat: String? = null
            var lon: String? = null
            var tz: String? = null
            var city: String? = null
            var id: String? = null
            var update: UpdateBean? = null

            class UpdateBean {
                /**
                 * loc : 2019-02-18 14:58
                 * utc : 2019-02-18 06:58
                 */

                var loc: String? = null
                var utc: String? = null
            }
        }

        class UpdateBeanX {
            /**
             * loc : 2019-02-18 14:58
             * utc : 2019-02-18 06:58
             */

            var loc: String? = null
            var utc: String? = null
        }

        class NowBean {
            /**
             * cloud : 99
             * cond_code : 104
             * cond_txt : 阴
             * fl : -2
             * hum : 70
             * pcpn : 0.0
             * pres : 1023
             * tmp : 0
             * vis : 14
             * wind_deg : 255
             * wind_dir : 西南风
             * wind_sc : 1
             * wind_spd : 2
             * cond : {"code":"104","txt":"阴"}
             */

            var cloud: String? = null
            var cond_code: String? = null
            var cond_txt: String? = null
            var fl: String? = null
            var hum: String? = null
            var pcpn: String? = null
            var pres: String? = null
            var tmp: String? = null
            var vis: String? = null
            var wind_deg: String? = null
            var wind_dir: String? = null
            var wind_sc: String? = null
            var wind_spd: String? = null
            var cond: CondBean? = null

            class CondBean {
                /**
                 * code : 104
                 * txt : 阴
                 */

                var code: String? = null
                var txt: String? = null
            }
        }

        class AqiBean {
            /**
             * city : {"aqi":"109","pm25":"82","qlty":"轻度污染"}
             */

            var city: CityBean? = null

            class CityBean {
                /**
                 * aqi : 109
                 * pm25 : 82
                 * qlty : 轻度污染
                 */

                var aqi: String? = null
                var pm25: String? = null
                var qlty: String? = null
            }
        }

        class SuggestionBean {
            /**
             * comf : {"type":"comf","brf":"较不舒适","txt":"白天会有降雪发生，您会感觉偏冷，不很舒适，请注意添加衣物。"}
             * sport : {"type":"sport","brf":"较不宜","txt":"有降雪，推荐您在室内进行低强度运动；若坚持户外运动，请选择合适运动并注意保暖。"}
             * cw : {"type":"cw","brf":"不宜","txt":"不宜洗车，未来24小时内有雪，如果在此期间洗车，雪水和路上的泥水可能会再次弄脏您的爱车。"}
             */

            var comf: ComfBean? = null
            var sport: SportBean? = null
            var cw: CwBean? = null

            class ComfBean {
                /**
                 * type : comf
                 * brf : 较不舒适
                 * txt : 白天会有降雪发生，您会感觉偏冷，不很舒适，请注意添加衣物。
                 */

                var type: String? = null
                var brf: String? = null
                var txt: String? = null
            }

            class SportBean {
                /**
                 * type : sport
                 * brf : 较不宜
                 * txt : 有降雪，推荐您在室内进行低强度运动；若坚持户外运动，请选择合适运动并注意保暖。
                 */

                var type: String? = null
                var brf: String? = null
                var txt: String? = null
            }

            class CwBean {
                /**
                 * type : cw
                 * brf : 不宜
                 * txt : 不宜洗车，未来24小时内有雪，如果在此期间洗车，雪水和路上的泥水可能会再次弄脏您的爱车。
                 */

                var type: String? = null
                var brf: String? = null
                var txt: String? = null
            }
        }

        class DailyForecastBean {
            /**
             * date : 2019-02-18
             * cond : {"txt_d":"小雪"}
             * tmp : {"max":"-1","min":"-9"}
             */

            var date: String? = null
            var cond: CondBeanX? = null
            var tmp: TmpBean? = null

            class CondBeanX {
                /**
                 * txt_d : 小雪
                 */

                var txt_d: String? = null
            }

            class TmpBean {
                /**
                 * max : -1
                 * min : -9
                 */

                var max: String? = null
                var min: String? = null
            }
        }
    }
}