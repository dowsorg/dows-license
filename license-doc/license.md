# 使用JDK自带的 keytool 工具生成公私钥证书库
公钥库密码：public_dows1234
私钥库密码为：private_dows1234
生成命令：
```shell
#生成命令
keytool -genkeypair -keysize 1024 -validity 3650 -alias "privateKey" -keystore "privateKeys.keystore" -storepass "public_dows1234" -keypass "private_dows1234" -dname "CN=localhost, OU=localhost, O=localhost, L=SH, ST=SH, C=CN"

#导出命令
keytool -exportcert -alias "privateKey" -keystore "privateKeys.keystore" -storepass "public_dows1234" -file "certfile.cer"

#导入命令
keytool -import -alias "publicCert" -file "certfile.cer" -keystore "publicCerts.keystore" -storepass "public_dows1234"
```


上述命令执行完成之后，会在当前路径下生成三个文件，分别是：`privateKeys.keystore`、`publicCerts.keystore`、`certfile.cer`
其中文件`certfile.cer`不再需要可以删除，文件`privateKeys.keystore`用于当前的 `license-app `项目给客户生成`license`文件，而文件`publicCerts.keystore`则随应用代码部署到客户服务器，用户解密`license`文件并校验其许可信息。


```json
{
  "consumerAmount": 1,
  "consumerType": "User",
  "description": "xx school",
  "issuedTime": "2023-03-28 15:36:10",
  "expiryTime": "2023-03-28 16:36:10",
  "keyPass": "private_dows1234",
  "storePass": "public_dows1234",
  "privateAlias": "privateKey",
  "subject": "dows_license",
  "privateKeysStorePath": "",
  "licensePath": "",
  "licenseCheckModel": {
    "ipAddress": [

    ],
    "macAddress": [
      "64-6E-E0-5A-26-8E"
    ],
    "cpuSerial": "BFEBFBFF000806C1",
    "mainBoardSerial": "L1HF14L017G"
  }
}
```