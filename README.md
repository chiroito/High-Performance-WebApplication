# Webアプリに低レイテンシ・高可用性を求めるのは間違っているのだろうか  
このソースコードは 2017年12月20日に実施した以下の講演で使用したソースコードです。  
※この内容は個人の見解であり、所属する組織の見解や製品機能等を保証するものではありません。  

Tech Deep Dive #0  
Webアプリに低レイテンシ・高可用性を求めるのは間違っているのだろうか  
[https://connpass.com/event/72517/:title]  

講演資料：

[https://www.slideshare.net/ChihiroIto1/web-84701590]

Github では以下を公開する予定です。
 - アプリケーションのソースコード
 - Dockerfile
 - Gatlingで実行するテストコード
 
## 環境
本アプリケーションは以下のインフラが必要です。
 - Oracle WebLogic Server (以下WebLogic)
 - Oracle Coherence (以下Coherence、試すだけでしたらOracle WebLogic Serverと同居も可能です)
 - データベースサーバ

Coherence は WebLogicに内包されているため、最小構成では WebLogic とデータベースサーバだけが必要です。
以下では、WebLogic と Coherence を単一プロセスで作成し、`localhost:7001`を使う例を紹介します。

### アプリケーションサーバとIn-Memory Data Gridサーバ 
WebLogic と Coherence は以下の構成が利用できます。
  - オンプレミス上にインストールした WebLogic および Coherence
  - Docker 上に構築した　WebLogic および Coherence ([https://github.com/chiroito/High-Performance-WebApplication/blob/master/docker/README.md])
  - Oracle Java Cloud Service
  
### データベースサーバ
データベースサーバは任意のデータベースを作成し、テーブルを作成して下さい。
テーブルの作成は CreateTable.md をご覧下さい

### アプリケーションサーバの設定
WebLogic の設定として以下が必要です。
 - データソースの設定
 - キャッシュの設定 (Coherenceクラスタの設定)

#### データソースの設定
データベースは WebLogic Server Administration Console 上の`サービス`-`データ・ソース`から作成できます。
作成したデータソースの`JNDI名`は`jdbc/TechDeepDive`にします。

#### キャッシュの設定
`環境`-`Coherenceクラスタ`から作成します。ターゲットではAdminServerにチェックを入れます。
`環境`-`サーバ`で`AdminServer`を選択します。`Coherence`タブでは`Coherenceクラスタ`で先ほど作成したクラスタが選択されていることを確認し、`ローカル・ストレージを有効化`をチェックします。


 # アプリケーション
 Maven を使ってビルドするには Oracle Maven Repository へのユーザ登録（無料）が必要です。  
 登録の手順は以下をご参照下さい。  
 [http://chiroito.hatenablog.jp/entry/2017/04/04/005550:title]
   
 1. 本レポジトリをクローン
 1. パッケージングにより EAR を作成
 1. EAR を WebLogic Server へデプロイ 

以下の例ではユーザ名`weblogic`、パスワードが`abcde`としています。WebLogicのインストール時に指定したパスワードを指定して下さい。Docker Hubにある公式イメージを使った場合には、初回起動時にパスワードが決定します。`docker logs <コンテナID>` の結果の先頭にパスワードが出力されていますのでご確認下さい。
 ```bash
git clone https://github.com/chiroito/High-Performance-WebApplication.git
cd High-Performance-WebApplication
mvn package
mvn weblogic:deploy -pl ear -Dadminurl=t3://localhost:7001 -Duser=weblogic -Dpassword=abcde -Dtargets=AdminServer
```

ブラウザで`http://localhost:7001/web/`へアクセスして下さい。
初期化をクリックすることで、データベースおよびキャッシュが初期化されます。  

管理用URL
 - サンプルトップページ (http://localhost:7001/web/)
 - 初期化ページ (http://localhost:7001/web/rest/init)
 - キャッシュ確認 (http://localhost:7001/web/rest/show)
 
 初期化ページは GET パラメータで商品数量(`quantity`)と商品数(`productNum`)、ユーザ数(`userNum`)を変更できます。  
デフォルトでは商品数量は`1,000,000`、商品数とユーザ数は`10,000`です。

変更の例：
```bash
curl http://localhost:7001/web/rest/init?quantity=10000&productNum=1000
```
  
  
購入URL 
 - SQL版 (http://localhost:7001/web/rest/db)
 - Cache版 (http://localhost:7001/web/rest/cache)
 - In-place版 (http://localhost:7001/web/rest/ep)
 
GET パラメータで購入数量(`quantity`)と商品ID(`productNo`)、ユーザID(`userId`)が必須です。  
 
 ```bash
 curl http://localhost:7001/web/rest/ep?productNo=1&quantity=1&userId=0
 ```
 
# WebLogic と Coherenceを別にする場合
1 つにまとめるケースでは本作業は不要です。管理対象サーバを 1 つ作成します。
役割ごとに以下の様に設定します

 WebLogic
  - ear ファイルをデプロイします
  - `ローカル・ストレージを有効化`をチェックしない
  - データソースを設定します。
  
 Coherence
  - gar ファイルをデプロイします
  - `ローカル・ストレージを有効化`をチェックします
  - データソースは不要です
  
  garファイル は`mvn package`で`gar`ディレクトリ内の`target`ディレクトリに作成されています。