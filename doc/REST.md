# REST 

Summary :
* Get data
* Actions

## Get data
* ```/monitor/rest/info```
* ```/monitor/rest/latest```
* ```/monitor/rest/longest```
* ```/monitor/rest/longestByUrl```

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

### /monitor/rest/latest
* Empty
```
{
"latest": [
]
}
```

* With logs
```
{
"latest": [
{
"startTime": 1409305861894,
"countAllRequest": 1,
"countLongTimeRequests": 1,
"url": "http://localhost:8080/telosys-timeout/timeout?t=100",
"elapsedTime": 105
},
{
"startTime": 1409305866395,
"countAllRequest": 2,
"countLongTimeRequests": 2,
"url": "http://localhost:8080/telosys-timeout/timeout?t=101",
"elapsedTime": 105
},
{
"startTime": 1409305869515,
"countAllRequest": 3,
"countLongTimeRequests": 3,
"url": "http://localhost:8080/telosys-timeout/timeout?t=102",
"elapsedTime": 104
}
]
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

### /monitor/rest/longestByUrl
* Empty
```
{
"longestByUrl": [
]
}
```

* With logs
```
{
"longestByUrl": [
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