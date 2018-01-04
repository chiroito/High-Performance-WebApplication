#!/bin/bash
. setEnv.sh
test_name=$1

# テスト用のログディレクトリ作成
mkdir -p ${log_dir}/${test_name}

# テストの結果を移動
mv ${gatling_home}/results/reports/* ${log_dir}/${test_name}/

# テストの実行ログを移動
mv ${test_name}.txt ${log_dir}/${test_name}/

# docker のリソース使用量をdockerホストから取得・dockerホスト上から消去
scp ${docker_user}@${docker_host}:~/docker/docker_stats.log ${log_dir}/${test_name}/
ssh -l ${docker_user} ${docker_host} 'rm -f ~/docker/docker_stats.log'
