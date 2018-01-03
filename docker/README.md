# Docker 上へ環境の構築方法
このディレクトリに含まれるスクリプト群を使用することで、Docker 上に必要な環境を全て構築します。この環境では WebLogic と Coherence は別のコンテナとして稼働します。

[https://github.com/chiroito/High-Performance-WebApplication/blob/master/README.md]

データベース、WebLogic、Coherenceをそれぞれ起動します。


1. ビルド成果物の配置
1. JDBC ドライバの配置
1. 設定ファイルの修正
1. 環境の起動/停止

上記は単一の Docker ホスト上で動かすことを前提としていますが、複数の Docker ホストで動かす場合にはこちらを参照して下さい。
- 複数の Docker ホストで動かす

## 1. ビルド成果物の配置
ビルド成果物を`container-scripts`ディレクトリへコピーします。

コピーするビルド成果物は以下の 2 つです。
- gar/target/web-application-gar.gar
- ear/target/web-application.ear

## 2. JDBC ドライバの配置
使用する RDBMS の JDBC ドライバをこのディレクトリにコピーします。


## 3. 設定ファイルの修正
以下 2 つの設定ファイルを修正します。
- コンテナの設定
- データベース接続の設定

### コンテナの設定
アプリケーションションサーバおよびデータストアのリソース量の設定と、起動するアプリケーションサーバのコンテナ数を設定します。リソースはCPU数とメモリ量を指定できます。

setEnv.sh
- アプリケーションサーバのCPU数：`ap_cpu_num` (初期設定値：`4.0`)
- アプリケーションサーバのメモリ量：`ap_mem_size` (初期設定値：`4g`)
- 起動するアプリケーションサーバのコンテナ数：`ap_container_num` (初期設定値：`2`)
- データストアのCPU数：`ds_cpu_num` (初期設定値：`1.0`)
- データストアのメモリ量：`ds_mem_size` (初期設定値：`4g`)

### データベース接続の設定
アプリケーションサーバからデータベースへ接続する情報を設定します。使用する RDBMS および 接続するデータベースにあわせて設定して下さい。

container-scripts/datasource.properties
- データソースドライバ：`dsdriver` (初期設定値：`org.postgresql.Driver`)
- データソース名：`dsname` (初期設定値：`Postgres`)
- データベース接続識別子：`dsurl` (初期設定値：`jdbc:postgresql://postgres4webapp:5432/postgres`)
- データベース名：`dsdbname` (初期設定値：`postgres`)
- データベースのユーザ：`dsusername` (初期設定値：`postgres`)
- データベースのパスワード：`dspassword` (初期設定値：`postgres`)
- 接続試験クエリ：`dstestquery` (初期設定値：`SELECT 1`)

## 4. 環境の起動/停止
- 起動
- ログの確認
- リソースの使用状況の確認
- 停止

### 起動
設定ファイルを使用して Docker イメージのビルドおよびコンテナを起動します。`Postgres`および`Coherence`のコンテナはそれぞれ 1 つ、`Weblogic`のコンテナは`ap_instance_num`で指定した数まで起動します。デフォルトの設定で起動するコンテナは`postgres4webapp`、`coherence1`、`weblogic1`、`weblogic2`です。
```bash
./build.sh
./run.sh
```
WebLogic が起動するとアプリケーションへアクセスできるようになります。

トップページ ([http://localhost:7001/web/])

アクセスする URL 一覧はこちらを参照下さい。

[https://github.com/chiroito/High-Performance-WebApplication/blob/master/README.md#%E3%82%A2%E3%83%97%E3%83%AA%E3%82%B1%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3]

### ログの確認
アプリケーションにアクセスできない場合にはログを確認します。コンテナの標準出力と AP サーバとして使用している WebLogic Server のログを確認します。コンテナの標準出力を確認するには`showStdout.sh`を実行し、WebLogic Server のログを確認するには`showWebLogicLog.sh`を実行します。どちらのスクリプトファイルも引数としてコンテナ名（coherence1,weblogic1,weblogic2など）が必要です。
```bash
./showStdout.sh weblogic1
./showWebLogicLog.sh weblogic1
```

### リソースの使用状況の確認
起動したそれぞれのコンテナが使用しているリソース量を出力します。
```bash
./stats.sh
```

### 停止
起動した全てのコンテナを停止し、全てのコンテナ及び全てのイメージを削除します。
```bash
./stop.sh
./remove.sh
```

## Appendix : 複数の Docker ホストで動かす
Swarm mode を有効にし、overlay ネットワークを構築します。詳細は省略しますが、以下を参考に構築手順を検索して下さい。

### Swarm modeを有効化
```bash
docker swarm init --advertise-addr 192.168.0.1
docker swarm join --token XXX 192.168.0.1:2377
```

### overlay ネットワークを構築
run.sh のうち以下の行に`-d overlay`を追加します。
```bash
docker network create --attachable webapp-net
```
修正後
```bash
docker network create -d overlay --attachable webapp-net
```