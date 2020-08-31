#!/bin/bash
#Purpose:Restart Structured Data Decoding and Storage Program for CMADAAS.
#Edit by LR

#Users modify program names or Pass program name parameters that need to be restarted.
#--------------------------------------------------------------------------------------
PRONAME='dpc_radi_bufr_hour.jar'
PORTNUM='6307'
CONFNAME='config/dpc_radi_bufr_hour.properties'
#--------------------------------------------------------------------------------------
SCRIPTNAME=$0
#Process number
PROCESSNUM=$(ps aux | grep "$PRONAME $PORTNUM $CONFNAME" | grep -v grep | grep -v "$SCRIPTNAME" | awk '{print $2}')
#echo "PID:"$PROCESSNUM



start() {
  # script name 
  # Count the number of "XXXXX.jar" processes and Remove excess "grep" records and "restart_CmadaasPro" records.
  PRO_CNT=$(ps aux | grep -s "$PRONAME $PORTNUM $CONFNAME" | grep -v grep | grep -v "$SCRIPTNAME" | wc -l)

  # Check whether the process exists,0 is not exists.
  # < 1
  if [ $PRO_CNT -lt 1 ]
  then
    cd ..
    cd bin
    echo "start program......"
    #nohup java -jar xxxxxxxx.jar xxxx config/xxxxxxxx.properties >/dev/null 2>&1 &
    nohup java -jar $PRONAME $PORTNUM $CONFNAME >/dev/null 2>&1 &

    echo 'start process:'`ps aux | grep -v grep | grep -v "$SCRIPTNAME" | grep "$PRONAME $PORTNUM $CONFNAME"`

  # > 1 
  elif [ $PRO_CNT -gt 1 ]
  then 
    echo "ERROR:Program="$PRONAME $PORTNUM $CONFNAME",The system runs processes with the SAME NAME,finds "$PRO_CNT" times, It is not possible to confirm which process it is,Exit script without doing anything!"
  # == 1
  else 
    PSNAME=`ps aux | grep -v grep | grep $PROCESSNUM`
    #echo 'echo ps:'$PSNAME
    kill -9 $PROCESSNUM
    PRO_CNT=$(ps aux | grep -s "$PRONAME $PORTNUM $CONFNAME" | grep -v grep | grep -v "$SCRIPTNAME" | wc -l)
    if [ $PRO_CNT -lt 1 ]
    then
      echo "restart program......"
      cd ..
      cd bin     
      #nohup java -jar xxxxxxxx.jar xxxx config/xxxxxxxx.properties >/dev/null 2>&1 &
      nohup java -jar $PRONAME $PORTNUM $CONFNAME >/dev/null 2>&1 &
          
      echo 'restart process:'`ps aux | grep -v grep | grep -v "$SCRIPTNAME" | grep "$PRONAME $PORTNUM $CONFNAME"`
    
    else
      echo "RestartError:The process: '"$PSNAME"' is not KILL,Restart failure!"
    fi
  fi
}

stop() {
  PRO_CNT=$(ps aux | grep -s "$PRONAME $PORTNUM $CONFNAME" | grep -v grep | grep -v "$SCRIPTNAME" | wc -l)
  if [ $PRO_CNT -lt 1 ]
  then
    echo 'The system is not running this process now!'
  elif [ $PRO_CNT -gt 1 ]
  then
    echo "ERROR:Program="$PRONAME $PORTNUM $CONFNAME",The system runs processes with the SAME NAME,finds "$PRO_CNT" times, It is not possible to confirm which process it is,Exit script without doing anything!"
  else
    PSNAME=`ps aux | grep -v grep | grep $PROCESSNUM`
    #echo 'echo ps:'$PSNAME
    kill -9 $PROCESSNUM
    PRO_CNT=$(ps aux | grep -s "$PRONAME $PORTNUM $CONFNAME" | grep -v grep | grep -v "$SCRIPTNAME" | wc -l)
    if [ $PRO_CNT -ge 1 ]
    then
      echo "StopError:The process: '"$PSNAME"' is not KILL!"
    else
      echo "Stop Program Success...."
    fi
  fi   
}


#{start|stop}
case "$1" in
   'start')
      start
      ;;
   'stop')
      stop
      ;;
   'restart')
      start
      ;;  
  *)
     echo "Usage: $0 {start|stop|restart}"
     exit 1
esac
exit 0