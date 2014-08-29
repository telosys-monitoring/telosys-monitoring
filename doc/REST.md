# REST 

Summary :
* Get data
* Actions

## Get data
* ```/monitor/rest/info```
* ```/monitor/rest/log```
* ```/monitor/rest/longest```
* ```/monitor/rest/top```

### /monitor/rest/info
```
{
"monitoring": {
"count_long_time_requests": 1,
"count_all_request": 1,
"activated": "true",
"initialization_date": "2014/08/28 17:39:37"
},
"host": {
"ip_adress": "192.168.10.183",
"java_vendor": "Sun Microsystems Inc.",
"os_name": "Windows 7",
"os_arch": "amd64",
"hostname": "FR-44-02-13-023",
"java_version": "1.6.0_45",
"os_version": "6.1"
},
"configuration": {
"duration": 1,
"url_params_activated": "false",
"url_params_empty": "true",
"url_params_filter": "*",
"log_size": 15,
"by_url_size": 5,
"by_time_size": 20
}
}
```

### /monitor/rest/log
* Empty
```
{
"log": [
]
}
```

* With logs
```
{
"host": {
"ip_adress": "192.168.56.1",
"hostname": "FR-44-02-13-023",
"java_version": "1.6.0_45",
"java_vendor": "Sun Microsystems Inc.",
"os_arch": "amd64",
"os_name": "Windows 7",
"os_version": "6.1"
},
"monitoring": {
"activated": "true",
"initialization_date": "2014/08/29 11:34:53",
"count_all_requests": 0,
"count_long_time_requests": 0
},
"configuration": {
"duration": 1,
"latest_size": 100,
"longest_size": 10,
"longest_by_url_size": 10,
"url_params_activated": "false",
"url_params_filter": "*",
"url_params_empty": "true"
}
}
```

### /monitor/rest/longest
* Empty
```
{
"longest": [
]
}
```

* With logs
```
{
"longest": [
{
"startTime": 1409238876151,
"countAllRequest": 3,
"countLongTimeRequests": 3,
"url": "http://localhost:8080/telosys-timeout/timeout?t=110",
"elapsedTime": 112
},
{
"startTime": 1409238846253,
"countAllRequest": 1,
"countLongTimeRequests": 1,
"url": "http://localhost:8080/telosys-timeout/timeout?t=100",
"elapsedTime": 105
}
]
}
```

### /monitor/rest/top
* Empty
```
{
"top": [
]
}
```

* With logs
```
{
"top": [
{
"startTime": 1409238876151,
"countAllRequest": 3,
"countLongTimeRequests": 3,
"url": "http://localhost:8080/telosys-timeout/timeout?t=110",
"elapsedTime": 112
},
{
"startTime": 1409238846253,
"countAllRequest": 1,
"countLongTimeRequests": 1,
"url": "http://localhost:8080/telosys-timeout/timeout?t=100",
"elapsedTime": 105
},
{
"startTime": 1409238847806,
"countAllRequest": 2,
"countLongTimeRequests": 2,
"url": "http://localhost:8080/telosys-timeout/timeout?t=100",
"elapsedTime": 102
}
]
}
```

# Actions

Type of Action | parameter 
------ | --------- 
Reset | action=reset
Clear | action=clear
Stop | action=stop               
Start | action=start
Duration threshold | duration=[value]
Max number of latest requests | latest_size=[value]
Max number of longest requests | longest_size=[value]
Max number of longest requests sorted by url | longest_by_url_size=[value]
Activate catching of URL parameters | url_params_activated=[true / false]
URL parameters name filter | url_params_filter=[* / name1,name2,...]
Display URL parameters without value | url_params_empty=[true / false]