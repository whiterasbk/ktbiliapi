# ktbliapi
[bilibili-API-collect](https://github.com/SocialSisterYi/bilibili-API-collect) 的 一个 `kotlin` 实现

## 安装
1. 添加 `jitpack` 仓库
   - `build.gradle`
      ```groovy
      repositories {
        maven { url 'https://www.jitpack.io' }
      }
      ```
   - `build.gradle.kts`
      ```groovy
      repositories {
          maven { url = uri("https://www.jitpack.io") }
      }
      ```
2. 添加依赖
   ```groovy
   dependencies {
       implementation("com.github.whiterasbk:ktbiliapi:latest_version")
   }
   ```

## 简单示例

1. 通过 bv 号获取视频流

    ```kotlin
    // 获取这个视频的第 1P
    val responseBody = getDashResponseBodies("bv").first()
    // 获取视频流的真实地址
    val url = getVideoStreamUrl(responseBody)
    ```

2. 登录
    ```kotlin
     val viaQRCode = LoginViaQRCodeMethod()
     // 获取登录用的链接并转化成二维码, 展示并提示用户扫码
     val url = viaQRCode.getQRCodeUrl()
     // 等待用户扫码, 完成后会返回用户的登录凭证
     val userCtx = viaQRCode.login()
    ```
   
3. 登录状态下获取视频流地址
   ```kotlin
   // 使用网页端二维码登录过程中获取到的 userContext
   val login = createWebQRCodeLoginContext(userContext)
   val responseBody = login {
      getDashResponseBodies("bv").first()
   }
   val url = getVideoStreamUrl(responseBody)
   ```
