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
"countAllRequest": 0,
"countLongTimeRequests": 0,
"activated": "true",
"initializationDate": "2014/08/28 17:06:13"
},
"host": {
"os.version": "6.1",
"ipAdress": "192.168.10.183",
"os.arch": "amd64",
"java.version": "1.6.0_45",
"hostname": "FR-44-02-13-023",
"os.name": "Windows 7",
"java.vendor": "Sun Microsystems Inc."
},
"configuration": {
"urlParamsActivated": "No",
"longestSize": 5,
"logSize": 15,
"urlParamsFilter": "*",
"topTenSize": 20,
"durationThreshold": 1
}
}
```

### /monitor/rest/log
#### Empty
```
{
"log": [
]
}
```

#### With logs
```
{
"log": [
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
},
{
"startTime": 1409238876151,
"countAllRequest": 3,
"countLongTimeRequests": 3,
"url": "http://localhost:8080/telosys-timeout/timeout?t=110",
"elapsedTime": 112
}
]
}
```

### /monitor/rest/longest
#### Empty
```
{
"longest": [
]
}
```

#### With logs
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
#### Empty
```
{
"top": [
]
}
```

#### With logs
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

action | parameter 
------ | --------- 
reset | action=reset
clear | action=clear
stop | action=stop               
start | action=start
duration threshold | duration=[value]
max number of requests in the log | log_size=[value]
max number of requests sorted by time | by_time_size=[value]
max number of requests sorted by url | by_url_size=[value]
activate catching of URL parameters | url_params_activated=[true|false]
URL parameters name filter | url_params_filter=[*|name1,name2,...]
display URL parameters without value | url_params_empty=[true|false]