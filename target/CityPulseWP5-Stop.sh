
#!/bin/bash

# kill the previous reasoning process 
PID=`ps -eaf | grep CityPulseWP5 | grep -v grep | awk '{print $2}'`
if [[ "" !=  "$PID" ]]; then
  echo "killing $PID"
  kill -9 $PID
fi