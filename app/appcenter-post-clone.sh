#/bin/bash
LOG=bintray.log
SLOW_LOG=bintray_slow.log
SLOW_RESPONSE=5.000

for i in {1..100}
do 
    OUTPUT="$(curl https://jcenter.bintray.com/com/android/tools/lint/lint-checks/25.1.3/lint-checks-25.1.3.pom -vs -w '\ntime_namelookup=%{time_namelookup}\ntime_appconnect=%{time_appconnect}\ntime_connect=%{time_connect}\ntime_redirect=%{time_redirect}\ntime_pretransfer=%{time_pretransfer}\ntime_starttransfer=%{time_starttransfer}\ntime_total=%{time_total}\n\n' &> /dev/stdout | tee -a ${LOG})"
    TIME_TOTAL=$(sed -n 's/time_total=//p' <<< "$OUTPUT")
    echo $TIME_TOTAL
    if [ $TIME_TOTAL \> $SLOW_RESPONSE ]
    then 
        echo "SLOW!!! ^"
        echo "$OUTPUT" >> $SLOW_LOG
        echo "$OUTPUT"
    fi
done