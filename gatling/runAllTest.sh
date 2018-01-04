#!/bin/bash
. setEnv.sh

# ウォーミングアップ
./dist-gatling.sh test.db.WarmingUpDb
./dist-gatling.sh test.cache.WarmingUpCache
./dist-gatling.sh test.ep.WarmingUpEp

for test_case in Db Cache Ep; do
for test_type in Single Multi; do
for num in {1..1}; do

# データストアを初期化
curl "http://${docker_host}:7001/web/rest/init?quantity=1000000&productNum=10000"

test_name=${test_case}${test_type}test${num}
pkg_name=test.`echo ${test_case} | sed 'y/CDE/cde/'`

# docker のリソース使用量を取得開始
ssh -l ${docker_user} ${docker_host} 'cd docker; nohup ./stats.sh > /dev/null &'

# テストを実行
./dist-gatling.sh ${pkg_name}.Pt${test_case}${test_type}Item | tee ${test_name}.txt

# docker のリソース使用量の取得を停止
ssh -l ${docker_user} ${docker_host} 'pkill -kill -f "docker stats"'

# テストの結果のログを収集・消去
./log.sh ${test_name}

done
done
done

