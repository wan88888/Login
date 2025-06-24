# API 参考文档

## 概述

本文档提供了 Android 登录应用所有公共 API、组件和方法的详细参考信息。

## 目录

- [UI 组件](#ui-组件)
- [ViewModel](#viewmodel)
- [数据模型](#数据模型)
- [验证器](#验证器)
- [测试工具](#测试工具)
- [主题和样式](#主题和样式)

## UI 组件

### MainActivity

应用的主入口点，负责设置主题和启动应用。

```kotlin
class MainActivity : ComponentActivity()
```

#### 方法

##### `onCreate(savedInstanceState: Bundle?)`

初始化应用并设置 Compose 内容。

**参数:**
- `savedInstanceState: Bundle?` - 保存的实例状态

**示例:**
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
        LoginTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                LoginApp()
            }
        }
    }
}
```

---

### LoginApp

主要的 Compose 应用组件，处理应用级别的状态和导航。

```kotlin
@Composable
fun LoginApp()
```

#### 状态

- `isLoggedIn: Boolean` - 当前登录状态

#### 行为

- 根据登录状态显示相应的屏幕
- 处理登录/登出状态切换

**示例:**
```kotlin
@Composable
fun LoginApp() {
    var isLoggedIn by remember { mutableStateOf(false) }
    
    if (isLoggedIn) {
        HomeScreen(onLogout = { isLoggedIn = false })
    } else {
        LoginScreen(onLoginSuccess = { isLoggedIn = true })
    }
}
```

---

### LoginScreen

登录界面组件，提供用户名和密码输入功能。

```kotlin
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit)
```

#### 参数

- `onLoginSuccess: () -> Unit` - 登录成功回调函数

#### 状态

- `username: String` - 用户名输入
- `password: String` - 密码输入
- `errorMessage: String` - 错误信息

#### UI 元素

| 元素 | Test Tag | 类型 | 描述 |
|------|----------|------|------|
| 标题 | `login_title` | Text | 显示"欢迎登录" |
| 用户名输入框 | `username_field` | OutlinedTextField | 用户名输入 |
| 密码输入框 | `password_field` | OutlinedTextField | 密码输入 |
| 错误信息 | `error_message` | Text | 显示验证错误 |
| 登录按钮 | `login_button` | Button | 执行登录操作 |

#### 验证规则

- 用户名和密码不能为空
- 正确凭据: `username="test"`, `password="123"`

**示例:**
```kotlin
LoginScreen(
    onLoginSuccess = {
        // 处理登录成功
        navController.navigate("home")
    }
)
```

---

### HomeScreen

主页界面组件，提供计数器功能和退出登录。

```kotlin
@Composable
fun HomeScreen(onLogout: () -> Unit = {})
```

#### 参数

- `onLogout: () -> Unit` - 退出登录回调函数（可选）

#### 状态

- `clickCount: Int` - 点击计数

#### UI 元素

| 元素 | Test Tag | 类型 | 描述 |
|------|----------|------|------|
| 标题 | `home_title` | Text | 显示"首页" |
| 计数显示 | `click_count_text` | Text | 显示当前点击次数 |
| 点击按钮 | `click_button` | Button | 增加计数 |
| 重置按钮 | `reset_button` | Button | 重置计数为0 |
| 退出按钮 | `logout_button` | TextButton | 退出登录 |

#### 功能

- **点击计数**: 每次点击增加1
- **重置计数**: 将计数重置为0
- **退出登录**: 返回登录页面

**示例:**
```kotlin
HomeScreen(
    onLogout = {
        // 处理退出登录
        authViewModel.logout()
    }
)
```

## ViewModel

### LoginViewModel

管理登录相关的业务逻辑和状态。

```kotlin
class LoginViewModel : ViewModel()
```

#### 状态流

##### `loginState: StateFlow<LoginState>`

登录状态的只读流。

```kotlin
val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
```

##### `homeState: StateFlow<HomeState>`

主页状态的只读流。

```kotlin
val homeState: StateFlow<HomeState> = _homeState.asStateFlow()
```

#### 方法

##### `updateUsername(username: String)`

更新用户名输入。

**参数:**
- `username: String` - 新的用户名

**副作用:**
- 清除当前错误信息

##### `updatePassword(password: String)`

更新密码输入。

**参数:**
- `password: String` - 新的密码

**副作用:**
- 清除当前错误信息

##### `login()`

执行登录验证。

**验证逻辑:**
1. 检查输入是否为空
2. 验证凭据是否正确
3. 更新登录状态或显示错误

##### `logout()`

执行退出登录。

**操作:**
- 重置所有状态
- 清除用户信息

##### `incrementCount()`

增加点击计数。

##### `resetCount()`

重置点击计数为0。

**示例:**
```kotlin
val viewModel = LoginViewModel()

// 更新用户名
viewModel.updateUsername("test")

// 更新密码
viewModel.updatePassword("123")

// 执行登录
viewModel.login()

// 观察状态
viewModel.loginState.collect { state ->
    if (state.isLoggedIn) {
        // 处理登录成功
    }
}
```

## 数据模型

### LoginState

登录相关状态的数据类。

```kotlin
data class LoginState(
    val username: String = "",
    val password: String = "",
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false
)
```

#### 属性

- `username: String` - 当前输入的用户名
- `password: String` - 当前输入的密码
- `errorMessage: String` - 当前错误信息
- `isLoggedIn: Boolean` - 是否已登录

---

### HomeState

主页相关状态的数据类。

```kotlin
data class HomeState(
    val clickCount: Int = 0
)
```

#### 属性

- `clickCount: Int` - 当前点击计数

## 验证器

### LoginValidator

提供登录输入验证功能的工具类。

```kotlin
class LoginValidator
```

#### 方法

##### `validateInput(username: String, password: String): String`

验证用户输入。

**参数:**
- `username: String` - 用户名
- `password: String` - 密码

**返回值:**
- `String` - 错误信息，空字符串表示验证通过

**验证规则:**
- 两个字段都为空: "用户名和密码不能为空"
- 只有用户名为空: "用户名不能为空"
- 只有密码为空: "密码不能为空"
- 都不为空: ""

##### `isValidCredentials(username: String, password: String): Boolean`

检查凭据是否有效。

**参数:**
- `username: String` - 用户名
- `password: String` - 密码

**返回值:**
- `Boolean` - 凭据是否有效

**有效凭据:**
- 用户名: "test"
- 密码: "123"

**示例:**
```kotlin
val validator = LoginValidator()

// 验证输入
val errorMessage = validator.validateInput("", "")
if (errorMessage.isNotEmpty()) {
    // 显示错误信息
}

// 检查凭据
val isValid = validator.isValidCredentials("test", "123")
if (isValid) {
    // 登录成功
}
```

## 测试工具

### 测试标签

应用中使用的测试标签常量：

```kotlin
object TestTags {
    // 登录页面
    const val LOGIN_SCREEN = "login_screen"
    const val LOGIN_TITLE = "login_title"
    const val USERNAME_FIELD = "username_field"
    const val PASSWORD_FIELD = "password_field"
    const val LOGIN_BUTTON = "login_button"
    const val ERROR_MESSAGE = "error_message"
    
    // 主页
    const val HOME_SCREEN = "home_screen"
    const val HOME_TITLE = "home_title"
    const val CLICK_COUNT_TEXT = "click_count_text"
    const val CLICK_BUTTON = "click_button"
    const val RESET_BUTTON = "reset_button"
    const val LOGOUT_BUTTON = "logout_button"
}
```

### 测试套件

#### LoginTestSuite

单元测试套件。

```kotlin
@RunWith(Suite::class)
@Suite.SuiteClasses(
    LoginViewModelTest::class,
    LoginValidationTest::class,
    CounterTest::class
)
class LoginTestSuite
```

#### EspressoTestSuite

UI 测试套件。

```kotlin
@RunWith(Suite::class)
@Suite.SuiteClasses(
    LoginEspressoTest::class,
    HomeScreenEspressoTest::class,
    EndToEndEspressoTest::class
)
class EspressoTestSuite
```

### 测试工具方法

#### `performLogin()`

辅助方法，在 Espresso 测试中执行登录操作。

```kotlin
private fun ComposeTestRule.performLogin() {
    onNodeWithTag("username_field").performTextInput("test")
    onNodeWithTag("password_field").performTextInput("123")
    onNodeWithTag("login_button").performClick()
    onNodeWithTag("home_screen").assertIsDisplayed()
}
```

## 主题和样式

### LoginTheme

应用主题组件。

```kotlin
@Composable
fun LoginTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
)
```

#### 参数

- `darkTheme: Boolean` - 是否使用深色主题
- `content: @Composable () -> Unit` - 主题内容

#### 特性

- Material Design 3 支持
- 动态颜色系统
- 深色/浅色主题切换
- 一致的字体规范

### 颜色方案

```kotlin
val colorScheme = if (darkTheme) {
    dynamicDarkColorScheme(LocalContext.current)
} else {
    dynamicLightColorScheme(LocalContext.current)
}
```

### 字体系统

使用 Material 3 默认字体规范：

```kotlin
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    // ... 其他字体样式
)
```

## 常量和配置

### 应用配置

```kotlin
object AppConfig {
    const val MIN_SDK = 28
    const val TARGET_SDK = 34
    const val COMPILE_SDK = 34
    
    // 登录凭据
    const val VALID_USERNAME = "test"
    const val VALID_PASSWORD = "123"
    
    // UI 配置
    const val DEFAULT_PADDING = 32 // dp
    const val BUTTON_HEIGHT = 50 // dp
}
```

### 字符串资源

主要字符串资源：

```xml
<resources>
    <string name="app_name">Login</string>
    <string name="login_title">欢迎登录</string>
    <string name="username_hint">请输入用户名</string>
    <string name="password_hint">请输入密码</string>
    <string name="login_button">登录</string>
    <string name="home_title">首页</string>
    <string name="click_button">点击</string>
    <string name="logout_button">退出登录</string>
</resources>
```

## 使用示例

### 完整的登录流程

```kotlin
// 1. 创建 ViewModel
val viewModel = LoginViewModel()

// 2. 观察状态
LaunchedEffect(Unit) {
    viewModel.loginState.collect { state ->
        if (state.isLoggedIn) {
            onNavigateToHome()
        }
    }
}

// 3. 处理用户输入
LoginScreen(
    onLoginSuccess = {
        // 登录成功处理
    }
)

// 4. 主页操作
HomeScreen(
    onLogout = {
        viewModel.logout()
    }
)
```

### 测试示例

```kotlin
// 单元测试
@Test
fun testLogin() {
    val viewModel = LoginViewModel()
    viewModel.updateUsername("test")
    viewModel.updatePassword("123")
    viewModel.login()
    
    val state = viewModel.loginState.value
    assertTrue(state.isLoggedIn)
}

// UI 测试
@Test
fun testLoginFlow() {
    composeTestRule.onNodeWithTag("username_field")
        .performTextInput("test")
    composeTestRule.onNodeWithTag("password_field")
        .performTextInput("123")
    composeTestRule.onNodeWithTag("login_button")
        .performClick()
    
    composeTestRule.onNodeWithTag("home_screen")
        .assertIsDisplayed()
}
```

## 错误处理

### 常见错误代码

| 错误 | 描述 | 解决方案 |
|------|------|----------|
| `EMPTY_FIELDS` | 用户名和密码都为空 | 提示用户填写信息 |
| `EMPTY_USERNAME` | 用户名为空 | 提示输入用户名 |
| `EMPTY_PASSWORD` | 密码为空 | 提示输入密码 |
| `INVALID_CREDENTIALS` | 凭据无效 | 提示检查用户名和密码 |

### 错误处理模式

```kotlin
when (errorType) {
    ErrorType.EMPTY_FIELDS -> "用户名和密码不能为空"
    ErrorType.EMPTY_USERNAME -> "用户名不能为空"
    ErrorType.EMPTY_PASSWORD -> "密码不能为空"
    ErrorType.INVALID_CREDENTIALS -> "用户名或密码错误"
    else -> ""
}
```

## 性能注意事项

### 状态管理最佳实践

- 使用 `remember` 缓存计算结果
- 避免在 Composable 中创建新对象
- 合理使用 `derivedStateOf`

### 测试性能优化

- 使用测试标签而非文本匹配
- 避免不必要的等待
- 并行执行无关联的测试

---

**注意**: 本 API 文档会随着应用更新而更新。建议定期查看最新版本。 