# TP1 / Step 0 改动说明

本次以 `2627_TP1_CRUD_EN.pdf` 为主要依据；新增的另一份文档是
`2627_TD2_AdvancedKotlin_En.pdf`（TD2，而非 TP2）。继续复用 TD2 法语业务模型，
按照 CMP 教程的起始布局拆分 Android 宿主和 UI 模块。本次不进行 iOS UI 迁移。

## 模块结构

- `androidApp`：Android 应用、Manifest、入口、图标及窗口主题。
- `composeApp/src/androidMain`：TP1 的 Compose 界面、ViewModel、导航和 Android 字符串资源。
- `shared/src/commonMain`：保留 TD2 的领域实体、目录，补充 TP1 仓库和模拟数据。
- 依赖方向：`androidApp -> composeApp -> shared`。
- 原 `app` 模块已不再被 Gradle 引入；本机残留的 ignored 测试和缓存保留。
- Gradle 项目名调整为 `SwapCampus_ZhengLi`，匹配 `SwapCampus_LastnameFirstname`。
  工作目录名称、Git 仓库名称及 Android applicationId 不随之修改。

## 构建、入口和资源

| 文件 | 改动和作用 |
|---|---|
| `settings.gradle.kts` | 引入 `androidApp`、`composeApp`、`shared`，规范项目显示名称 |
| `gradle/libs.versions.toml` | 增加 Coil 图片加载库及仅用于本地测试的协程测试依赖 |
| `.gitignore` | 延续测试不提交的要求，忽略新模块的构建输出和测试目录 |
| `androidApp/build.gradle.kts` | 从旧 app 配置迁移；Android 宿主只依赖 UI 模块及入口所需 Android 库 |
| `androidApp/proguard-rules.pro` | 从旧 app 原样移动，保留宿主构建配置 |
| `androidApp/.gitignore` | 从旧 app 移动，忽略宿主 build 输出 |
| `androidApp/src/main/AndroidManifest.xml` | 从旧 app 移动，注册入口和 Application，并增加网络图片权限 |
| `androidApp/src/main/kotlin/com/example/swapcampus_zheng_li/MainActivity.kt` | 移动并简化，只负责调用 `SwapCampusApp` |
| `androidApp/src/main/kotlin/com/example/swapcampus_zheng_li/SwapCampusApplication.kt` | 移动，继续持有应用级 `AppContainer`，避免旋转时新建仓库 |
| `androidApp/src/main/res/` | 移动原图标、颜色、窗口主题、备份规则等；显示名称改为 SwapCampus |
| `composeApp/build.gradle.kts` | 新增 KMP UI 模块，仅配置 Android 目标；启用 Compose 和 Android 资源 |
| `composeApp/src/androidMain/AndroidManifest.xml` | 新增 UI 库的最小 Manifest |
| `composeApp/src/androidMain/res/values/strings.xml` | 新增英文登录、错误、个人页文字和演示账号 |
| `composeApp/src/androidMain/res/values-fr/strings.xml` | 新增对应法文资源 |
| `composeApp/src/androidMain/res/drawable/listing_placeholder.xml` | 新增图片加载中／失败占位图 |
| `README.md` | 更新最终目录、构建命令、账号和 Step 0 验收步骤 |
| `docs/TP1_STEP0_CHANGES.md` | 本改动清单 |

## 界面代码

以下文件均位于 `composeApp/src/androidMain/kotlin/com/example/swapcampus_zheng_li/ui/`。

| 文件 | 来源与作用 |
|---|---|
| `AppContainer.kt` | 从旧 app 移动；统一仓库实例并通过仓库加入演示公告 |
| `SwapCampusApp.kt` | 从旧 `com/swapcampus/UIComponents.kt` 整理；顶层 ViewModel、主题、NavHost、选中状态正确的底部导航 |
| `viewmodel/AuthViewModel.kt` | 从旧 ui/auth 移动；登录、错误 StateFlow、当前用户及退出 |
| `viewmodel/ListingsViewModel.kt` | 从旧 ui/listings 移动；仅依赖仓库接口，负责筛选、写入、所有权和状态变更 |
| `viewmodel/UiState.kt` | 从 ListingsUiState 整理为 TP1 中的 UiState 命名；Loading / Success / Error |
| `screens/LoginScreen.kt` | 从旧 ui/auth 移动；表单、密码遮挡、输入保留、错误提示、登录成功返回上一页 |
| `screens/ProfileScreen.kt` | 从旧大文件拆出；游客登录入口、欢迎信息、退出及共用登录保护提示 |
| `screens/ListingsScreen.kt` | 合并原重复的 BrowseScreen 和 ListingsScreen；LazyColumn、LazyRow 分类、关键词筛选、三种状态和空结果 |
| `screens/ListingFormScreen.kt` | 从旧大文件拆出；发布／预填编辑表单；保存成功才返回；校验失败显示仓库错误 |
| `screens/ListingDetailScreen.kt` | 从旧大文件拆出；详情、预订、售出、取消、重新上架、所有者编辑／确认删除 |
| `screens/MyListingsScreen.kt` | 从旧大文件拆出；当前用户的公告，不受浏览页筛选影响 |
| `components/ListingCard.kt` | 抽取卡片和照片组件；URL 图片、缺图占位、标题、价格、分类和状态 |
| `navigation/Routes.kt` | 新增集中路由命名和整数 ID 参数 |
| `i18n/Labels.kt` | 新增 LoginError 到 Android 字符串资源的映射，为后续迁移准备 |
| `theme/Theme.kt` | 从旧 app 移动，保留明暗模式与 Android 动态颜色 |

原 `UIComponents.kt` 已由上述独立文件取代，不保留第二套 UI。

## shared：复用 TD2，补齐 TP1

以下文件均位于 `shared/src/commonMain/kotlin/com/example/swapcampus_zheng_li/`。

| 文件 | 改动和作用 |
|---|---|
| `CompteUtilisateur.kt` | 只修正原 `compteUtilisateur.kt` 文件名大小写，不改变实体字段 |
| `Dates.kt` | 新增跨平台日期助手，采用 `kotlin.time.Clock` 和 kotlinx-datetime |
| `repository/DemoData.kt` | 新增统一模拟账号和初始公告，含 `ada@isep.fr / ada123`；保留原 student/admin 账号 |
| `repository/InMemoryAuthRepository.kt` | 使用统一模拟账号；公开只读 StateFlow；继续采用 TP1 要求的明文模拟密码 |
| `repository/InMemoryListingRepository.kt` | 增加价格必须有限的校验，拒绝 NaN/Infinity；保留非空标题、正价格等校验 |

`Annonce.kt`、`EtatAnnonce.kt`、`Categorie.kt`、`CatalogueAnnonces.kt`、`Message.kt`、
`ActionModeration.kt`、`TypeActionModeration.kt`、`RoleUtilisateur.kt` 等 TD2 文件的
法语命名及数据结构保留。没有实现同学后续负责的消息、审核或 Firebase 功能。

## 本地验证文件（已忽略，不提交）

- `composeApp/src/androidHostTest/.../AuthViewModelTest.kt`：迁移已有本地登录测试。
- `composeApp/src/androidHostTest/.../ListingsViewModelTest.kt`：筛选、完整列表、ID、权限、状态流转。
- `shared/src/androidHostTest/.../ListingRepositoryTest.kt`：无 UI 条件下验证共享业务规则。

保留之前已经暂存的取消测试跟踪，以及其他原有改动；没有进行 commit 或 push。

## 本次验证结果

- `:androidApp:assembleDebug`：成功。
- `:androidApp:lintDebug`：通过，0 个 Error/Fatal。
- `:composeApp:testAndroidHostTest :shared:testAndroidHostTest`：7 个测试全部通过。
- API 37 模拟器：游客浏览、空登录、错误密码、Ada 登录、退出、未登录发布保护、
  发布公告、详情、所有者编辑、删除取消与确认删除均通过。
- 预订／售出／重新上架及非所有者权限另由本地 ViewModel 测试覆盖。
- APK：`androidApp/build/outputs/apk/debug/androidApp-debug.apk`。
- 验证日志与登录截图位于被忽略的 `build/reports/`。

这完成了 Step 0 的前置运行效果；工作树尚未提交。iOS UI 迁移和云端构建仍为后续步骤。
