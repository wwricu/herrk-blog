javascript:

$.ajax({
    type: 'GET',
    url: 'https://tianqiapi.com/api',
    data: {
        version: 'v6',
        appid: '37816743',
        appsecret: 'ua2qjGv7'
    },
    dataType: 'JSON',
    async: 'true',
    error : function() {
        console.log('weather ajax fail');
    },
    success: function(result) {

        var weather;
        switch(result.wea_img) {
            case 'bingbao':
                weather = 'hailing';
                break;
            case 'lei':
                weather = 'thunder storm';
                break;
            case 'shachen':
                weather = 'sand storm';
                break;
            case 'qing':
                weather = 'clear';
                break;
            case 'wu':
                weather = 'foggy';
                break;
            case 'xue':
                weather = 'snowy';
                break;
            case 'yin':
                weather = 'most cloudy';
                break;
            case 'yu':
                weather = 'rainy';
                break;
            case 'yun':
                weather = 'partly cloudy';
                break;
            default:
                weather = 'did not get';
                break;
        }

        $("#location").text(result.cityEn + ', ' + result.countryEn);
        $("#weather").text(weather);
        $("#temperature").text(result.tem);

    }
});