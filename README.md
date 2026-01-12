# FloatView

[![Maven Central](https://img.shields.io/maven-central/v/com.xeonyu/float-view.svg)](https://search.maven.org/artifact/com.xeonyu/float-view)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8-blue.svg)](https://kotlinlang.org)

一个功能强大、简单易用的 Android 浮动视图组件。支持拖动、贴边、视图切换等丰富的功能，提供了完整的生命周期管理和事件回调机制。

##  功能特点

- **三视图模式**: 支持内容视图、移动视图、贴边视图三种模式，可根据不同场景自定义显示
- **拖动支持**: 灵活配置是否支持拖动，满足不同交互需求
- **自动贴边**: 支持自动吸附到屏幕边缘,提升用户体验
- **自定义比例**: 可配置贴边视图的显示比例，实现部分显示效果
- **全局显示**: 支持跨页面全局显示，自动管理生命周期
- **灵活的黑白名单**: 支持类型、包名、类名多种匹配方式，完美适配组件化架构
- **手动控制**: 支持手动切换显示/隐藏、内容视图/贴边视图
- **事件回调**: 完善的点击事件监听，方便业务处理
- **动画效果**: 内置流畅的过渡动画，提升视觉效果
- **兼容性好**: 支持 API 21+ (Android 5.0+)，覆盖绝大部分设备
- **多实例支持**: 支持同时显示多个悬浮窗，通过 Tag 区分管理

## 安装

请在 [Maven Central](https://search.maven.org/artifact/com.xeonyu/float-view) 查看最新版本：[![Maven Central](https://img.shields.io/maven-central/v/com.xeonyu/float-view.svg)](https://search.maven.org/artifact/com.xeonyu/float-view)

### Gradle (Kotlin DSL)

在模块的 `build.gradle.kts` 文件中添加依赖：

```kotlin
dependencies {
    implementation("com.xeonyu:float-view:x.x.x") // 请使用最新版本号替换 x.x.x
}
```

### Gradle (Groovy)

在模块的 `build.gradle` 文件中添加依赖：

```groovy
dependencies {
    implementation 'com.xeonyu:float-view:x.x.x' // 请使用最新版本号替换 x.x.x
}
```

## 快速开始

### 最简单的用法

```kotlin
// 1. 创建悬浮视图管理器
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)  // 全局显示
    }
    .build()
    .show()
```

### 开启调试日志

库默认关闭日志输出，可在 Debug 模式下开启：

```kotlin
// 在 Application.onCreate() 中配置
FloatViewLog.isLoggable = BuildConfig.DEBUG
```

### 完整示例

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 创建悬浮视图布局
        val floatViewBinding = LayoutFloatViewBinding.inflate(LayoutInflater.from(this))

        // 设置内容
        floatViewBinding.contentView.setImageResource(R.drawable.icon_float)

        // 创建并显示悬浮视图
        FloatingViewManager.Builder(this)
            .floatingViewConfig {
                // 必需：设置内容视图
                contentView(floatViewBinding.contentView)
                
                // 可选：设置移动时显示的视图
                moveView(floatViewBinding.moveView)
                
                // 可选：设置贴边时显示的视图
                edgeView(floatViewBinding.edgeView)
                
                // 配置贴边视图显示比例 (0.0-1.0)
                edgeViewScale(0.7f)
                
                // 是否可拖动
                draggable(true)
                
                // 是否自动贴边
                autoMoveToEdge(true)
                
                // 贴边后是否自动显示贴边视图
                autoShowEdgeView(true)
                
                // 设置布局参数
                width(100)
                height(100)
                gravity(Gravity.START or Gravity.BOTTOM)
                bottomMargin(100)
                leftMargin(30)
                
                // 全局显示配置
                globalShow(true)  // 全局显示，自动管理生命周期
                // 可选：添加黑名单
                addBlackList(SecondActivity::class.java, ThirdActivity::class.java)
            }
            .build()
            .setFloatingViewListener(object : FloatingViewListener {
                override fun onClick() {
                    // 处理点击事件
                    Toast.makeText(this@MainActivity, "悬浮视图被点击", Toast.LENGTH_SHORT).show()
                }
            })
            .show()
    }
}
```

## 📖 详细使用说明

### 1. 基础用法（单页面显示）

适用于只需要在单个页面显示悬浮视图的场景：

```kotlin
val floatingViewManager = FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(false)  // 不全局显示
    }
    .build()

// 显示
binding.btnShow.setOnClickListener {
    floatingViewManager.show()
}

// 隐藏
binding.btnHide.setOnClickListener {
    floatingViewManager.hide()
}

// 切换到贴边视图
binding.btnSwitchToEdge.setOnClickListener {
    floatingViewManager.switchToEdgeView()
}

// 切换到内容视图
binding.btnSwitchToContent.setOnClickListener {
    floatingViewManager.switchToContentView()
}
```

### 2. 全局显示（跨页面）

适用于需要在多个页面显示悬浮视图的场景：

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)  // 全局显示，自动管理生命周期
        // 可选：添加黑名单，在指定页面不显示
        addBlackList(SplashActivity::class.java)
    }
    .build()
    .show()
```

### 3. 自定义三种视图

悬浮视图支持三种视图模式：

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        // 内容视图：默认显示的视图
        contentView(floatViewBinding.contentView)
        
        // 移动视图：拖动时显示的视图（可选）
        moveView(floatViewBinding.moveView)
        
        // 贴边视图：贴边时显示的视图（可选）
        edgeView(floatViewBinding.edgeView)
        
        // 贴边视图显示比例（只对 edgeView 有效）
        edgeViewScale(0.7f)  // 显示 70%
    }
    .build()
```

**视图切换逻辑**：
- 静止状态：显示 `contentView`
- 拖动中：显示 `moveView`（如果设置了）
- 贴边后：显示 `edgeView`（如果设置了）

### 4. 黑白名单机制

在全局显示模式下，支持灵活的黑白名单机制，特别适合**组件化架构**，无需依赖具体的 Activity 类。

#### 4.1 黑名单模式

黑名单模式适用于"默认显示，部分页面不显示"的场景。

##### 方式一：类型匹配（传统方式，保持向后兼容）

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)
        // 通过 Class 类型过滤
        addBlackList(
            SplashActivity::class.java,
            LoginActivity::class.java
        )
    }
    .build()
    .show()
```

##### 方式二：包名匹配（推荐用于组件化）

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)
        // 通过包名过滤（支持前缀匹配）
        addBlackListPackages(
            "com.example.feature.login",      // 登录模块的所有页面
            "com.example.feature.splash",     // 启动模块的所有页面
            "com.example.debug"               // Debug 模块的所有页面
        )
    }
    .build()
    .show()
```

##### 方式三：类名匹配

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)
        // 通过类名过滤（支持简单类名或全限定类名）
        addBlackListClassNames(
            "MainActivity",                    // 匹配任意包下的 MainActivity
            "com.example.LoginActivity"       // 精确匹配该类
        )
    }
    .build()
    .show()
```

##### 方式四：混合使用（最灵活）

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)
        
        // 使用 ActivityFilterRule 创建自定义规则
        addBlackList(
            ActivityFilterRule.ofClass(SplashActivity::class.java),
            ActivityFilterRule.ofPackage("com.example.feature.debug"),
            ActivityFilterRule.ofClassName("PaymentActivity")
        )
    }
    .build()
    .show()
```

#### 4.2 白名单模式

白名单模式适用于"默认不显示，只在指定页面显示"的场景。**白名单优先级高于黑名单**。

##### 方式一：类型匹配

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)
        // 只在主页和详情页显示
        addWhiteList(
            MainActivity::class.java,
            DetailActivity::class.java
        )
    }
    .build()
    .show()
```

##### 方式二：包名匹配（推荐用于组件化）

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)
        // 只在主页模块和商城模块显示
        addWhiteListPackages(
            "com.example.feature.home",
            "com.example.feature.mall"
        )
    }
    .build()
    .show()
```

##### 方式三：类名匹配

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)
        // 只在首页和商品详情页显示
        addWhiteListClassNames(
            "HomeActivity",
            "ProductDetailActivity"
        )
    }
    .build()
    .show()
```

#### 4.3 优先级说明

过滤规则的优先级：**白名单 > 黑名单**

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)
        
        // 白名单：只在主页模块显示
        addWhiteListPackages("com.example.feature.home")
        
        // 黑名单：在这种情况下会被忽略，因为白名单优先
        addBlackList(SplashActivity::class.java)
    }
    .build()
    .show()
```

**规则说明**：
1. 如果设置了白名单，只有匹配白名单的页面才会显示悬浮窗
2. 如果没有设置白名单，则检查黑名单，匹配黑名单的页面不显示
3. 如果既没有白名单也没有黑名单，默认所有页面都显示

#### 4.4 组件化场景最佳实践

在组件化架构中，主 App 模块通常无法直接依赖各个业务模块的 Activity 类，推荐使用**包名匹配**：

```kotlin
// 在主 App 模块中
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)
        
        // 黑名单：不在登录、启动、Debug 模块显示
        addBlackListPackages(
            "com.example.feature.login",
            "com.example.feature.splash",
            "com.example.debug"
        )
        
        // 或使用白名单：只在主页和商城模块显示
        // addWhiteListPackages(
        //     "com.example.feature.home",
        //     "com.example.feature.mall"
        // )
    }
    .build()
    .show()
```

### 5. 点击事件监听

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
    }
    .build()
    .setFloatingViewListener(object : FloatingViewListener {
        override fun onClick() {
            // 处理点击事件
            val intent = Intent(this@MainActivity, TargetActivity::class.java)
            startActivity(intent)
        }
    })
    .show()
```

### 6. 手动控制显示/隐藏

```kotlin
// 创建悬浮视图管理器
val floatingViewManager = FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(false)  // 手动控制模式
    }
    .build()

// 显示悬浮视图
floatingViewManager.show()

// 隐藏悬浮视图
floatingViewManager.hide()
```

### 7. 多悬浮窗支持

通过设置 `tag`，可以创建和管理多个独立的悬浮窗实例：

```kotlin
// 创建第一个悬浮窗
FloatingViewManager.Builder(this)
    .tag("FLOAT_A")
    .floatingViewConfig {
        contentView(R.layout.layout_float_a)
        leftMargin(0)
    }
    .build()
    .show()

// 创建第二个悬浮窗
FloatingViewManager.Builder(this)
    .tag("FLOAT_B")
    .floatingViewConfig {
        contentView(R.layout.layout_float_b)
        rightMargin(0)
    }
    .build()
    .show()
```

### 8. 自定义贴边距离与半隐藏效果

通过 `marginEdge` 属性可以控制贴边时的偏移量。设置为负值可实现"半隐藏"效果：

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view) // 假设宽度为 60dp
        
        // 设置贴边后的距离
        // 正值：距离边缘 10px
        // marginEdge(10) 
        
        // 负值：实现半隐藏效果 (例如向外偏移 30px)
        marginEdge(-30)
        
        // 配合 Gravity 使用初识位置
        gravity(Gravity.END or Gravity.CENTER_VERTICAL)
    }
    .build()
    .show()
```

## 🔧 API 文档

### FloatingViewManager.Builder

#### 构造方法

```kotlin
Builder(context: Context)
```

#### 配置方法

| 方法 | 说明 | 必需 |
|------|------|------|
| `tag(tag: String)` | 设置悬浮窗标签（用于多实例管理） | ❌ |
| `floatingViewConfig(block)` | 使用 Lambda 配置悬浮视图 | ✅ |
| `build()` | 构建悬浮视图管理器 | ✅ |

### FloatingViewConfig

#### 视图配置

| 方法 | 说明 | 必需 |
|------|------|------|
| `contentView(view: View)` | 设置内容视图（通过 View） | ✅ |
| `contentView(@LayoutRes layoutResId: Int)` | 设置内容视图（通过布局 ID） | ✅ |
| `moveView(view: View)` | 设置移动视图（通过 View） | ❌ |
| `moveView(@LayoutRes layoutResId: Int)` | 设置移动视图（通过布局 ID） | ❌ |
| `edgeView(view: View)` | 设置贴边视图（通过 View） | ❌ |
| `edgeView(@LayoutRes layoutResId: Int)` | 设置贴边视图（通过布局 ID） | ❌ |
| `globalShow(globalShow: Boolean)` | 设置是否全局显示 | ❌ |

#### 黑白名单配置

| 方法 | 说明 | 必需 |
|------|------|------|
| `addBlackList(vararg rules: ActivityFilterRule)` | 添加黑名单规则 | ❌ |
| `addBlackList(vararg clazz: Class<*>)` | 添加黑名单 Activity 类型 | ❌ |
| `addBlackListPackages(vararg packageNames: String)` | 添加黑名单包名 | ❌ |
| `addBlackListClassNames(vararg classNames: String)` | 添加黑名单类名 | ❌ |
| `addWhiteList(vararg rules: ActivityFilterRule)` | 添加白名单规则 | ❌ |
| `addWhiteList(vararg clazz: Class<*>)` | 添加白名单 Activity 类型 | ❌ |
| `addWhiteListPackages(vararg packageNames: String)` | 添加白名单包名 | ❌ |
| `addWhiteListClassNames(vararg classNames: String)` | 添加白名单类名 | ❌ |

#### 行为配置

| 方法 | 说明 | 默认值 |
|------|------|--------|
| `draggable(draggable: Boolean)` | 设置是否可拖动 | `true` |
| `autoMoveToEdge(autoMoveToEdge: Boolean)` | 设置是否自动贴边 | `true` |
| `autoShowEdgeView(autoShowEdgeView: Boolean)` | 设置贴边后是否自动显示贴边视图 | `true` |
| `edgeViewScale(scale: Float)` | 设置贴边视图的显示比例 (0.0-1.0) | `1.0f` |

#### 布局配置

| 方法 | 说明 | 默认值 |
|------|------|--------|
| `width(width: Int)` | 设置宽度 | `WRAP_CONTENT` |
| `height(height: Int)` | 设置高度 | `WRAP_CONTENT` |
| `gravity(@GravityInt gravity: Int)` | 设置重力 | `BOTTOM or END` |
| `topMargin(topMargin: Int)` | 设置顶部边距 | `0` |
| `bottomMargin(bottomMargin: Int)` | 设置底部边距 | `0` |
| `leftMargin(leftMargin: Int)` | 设置左边距 | `0` |
| `rightMargin(rightMargin: Int)` | 设置右边距 | `0` |
| `marginEdge(marginEdge: Int)` | 设置内容视图贴边时距离屏幕边缘的距离 | `0` |

### FloatingViewManager

| 方法 | 说明 |
|------|------|
| `show()` | 显示悬浮视图 |
| `hide()` | 隐藏悬浮视图 |
| `destroy()` | 销毁悬浮视图并释放资源 |
| `FloatingViewManager.get(tag)` | 根据 Tag 获取实例 (静态方法) |
| `FloatingViewManager.destroy(tag)` | 根据 Tag 销毁实例 (静态方法) |
| `switchToEdgeView()` | 切换到贴边视图 |
| `switchToContentView()` | 切换到内容视图 |
| `setFloatingViewListener(listener: FloatingViewListener)` | 设置点击事件监听器 |

### FloatingViewListener

```kotlin
interface FloatingViewListener {
    fun onClick()  // 点击事件回调
}
```

## 🎯 最佳实践

### 1. 使用 Lottie 动画

推荐使用 Lottie 动画提升视觉效果：

```kotlin
val floatViewBinding = LayoutFloatViewBinding.inflate(LayoutInflater.from(this))

// 设置 Lottie 动画
floatViewBinding.contentView.setAnimationFromUrl("https://assets7.lottiefiles.com/packages/lf20_5lTxAupekw.json")
floatViewBinding.contentView.setPadding(0)

FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(floatViewBinding.contentView)
        globalShow(true)
    }
    .build()
    .show()
```

### 2. 合理使用黑名单

在全局显示模式下，建议对以下页面使用黑名单：
- 启动页
- 登录页
- 全屏视频播放页
- 支付页

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(R.layout.layout_float_view)
        globalShow(true)
        addBlackList(
            SplashActivity::class.java,
            LoginActivity::class.java,
            FullScreenVideoActivity::class.java,
            PaymentActivity::class.java
        )
    }
    .build()
    .show()
```

### 3. 选择合适的显示比例

贴边视图的显示比例建议：
- `0.5f` - 半边显示（节省空间）
- `0.7f` - 常用比例（平衡显示和空间）
- `1.0f` - 完整显示（最大可见性）

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(floatViewBinding.contentView)
        edgeView(floatViewBinding.edgeView)
        edgeViewScale(0.7f)  // 推荐值
    }
    .build()
    .show()
```

### 4. 合理设置边距

根据屏幕尺寸和内容大小，合理设置边距：

```kotlin
FloatingViewManager.Builder(this)
    .floatingViewConfig {
        contentView(floatViewBinding.contentView)
        gravity(Gravity.START or Gravity.BOTTOM)
        bottomMargin(100)  // 底部边距，避免遮挡底部导航
        leftMargin(30)    // 左侧边距，避免贴边完全隐藏
    }
    .build()
    .show()
```

### 5. 内存管理

对于复杂的悬浮视图，建议在不需要时手动销毁，释放资源：

```kotlin
override fun onDestroy() {
    super.onDestroy()
    // 销毁悬浮视图（建议在非全局模式下使用）
    floatingViewManager.destroy()
}
```



## 贡献

欢迎贡献代码！如果你发现了 bug 或者有新的功能建议，请：

1. [创建 Issue](https://github.com/yuzhiqiang1993/float-view/issues)
2. [创建 Pull Request](https://github.com/yuzhiqiang1993/float-view/pulls)

## 许可证

本项目采用 [Apache License 2.0](LICENSE) 许可证。

---


如果这个项目对您有帮助，请给个 ⭐️ Star 支持一下！
