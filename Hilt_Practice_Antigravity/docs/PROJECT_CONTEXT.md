# 📋 PROJECT_CONTEXT — Hilt_Practice_Antigravity

> **목적**: 이 문서는 AI 어시스턴트(Antigravity)가 이 프로젝트를 처음 대화에서도 즉시 파악하고  
> 정확한 코드 리뷰·기능 추가·리팩토링 지원을 할 수 있도록 작성된 컨텍스트 파일입니다.  
> **마지막 업데이트**: 2026-05-28 (미사용 패키지·예제 테스트 정리)

---

## 1. 프로젝트 개요

| 항목 | 내용 |
|------|------|
| **프로젝트명** | Hilt_Practice_Antigravity |
| **패키지명** | `com.cret.hilt_practice` |
| **Application ID** | `com.cret.antigravity_hilt_practice` |
| **GitHub** | https://github.com/duswns261/Hilt-Practice-with-AI |
| **목적** | Manual DI(`AppContainer`) → Hilt 마이그레이션 학습 + 코드 품질 배포 수준까지 향상 |
| **현재 단계** | ✅ Hilt 적용 완료 / ✅ 코드 리뷰·리팩토링 13개 이슈 해결 / ✅ 단위 테스트 10개 작성 / ✅ Navigation 완성 |

### 학습 맥락

이 프로젝트는 단순 Hilt 튜토리얼이 아닙니다. 실제 코드베이스(InOutManager 메모 앱)에서  
`AppContainer` 패턴으로 시작해, Hilt 적용 후 코드 리뷰 → 리팩토링 → 테스트 추가까지  
실제 배포 수준의 품질을 목표로 진행된 학습 프로젝트입니다.

---

## 2. 기술 스택

| 분류 | 기술 | 버전 |
|------|------|------|
| Language | Kotlin | 2.0.21 |
| UI | Jetpack Compose | BOM 2024.12.01 |
| DI | Hilt (Dagger) | 2.56.1 |
| Architecture | MVVM + Clean Architecture | - |
| Async | Kotlin Coroutines + StateFlow | - |
| Test | JUnit4 + MockK + coroutines-test | MockK 1.13.10, coroutines-test 1.9.0 |
| Min SDK | 26 | Target SDK 36 |
| Build | AGP 8.13.2, Gradle KTS | - |
| Navigation | Hilt Navigation Compose | 1.2.0 |

---

## 3. 아키텍처

### 레이어 구조 (Clean Architecture)

```
data/                       — 구현체만 (model/ 없음, API·DB 추가 시 remote/·local/ 확장)
  repository/
    UserRepositoryImpl.kt   — @Inject constructor, domain 모델 반환, mock API

domain/
  model/
    User.kt                 — data class
    UserError.kt            — sealed class (NotFound / Network / Unknown)
  repository/
    UserRepository.kt       — interface, 반환 Result<User>
  usecase/
    GetUserUseCase.kt       — @Inject constructor, Repository 호출 래핑

presentation/
  model/
    UserUiModel.kt          — UI 전용 모델 (도메인 모델 노출 차단)
    UserUiState.kt          — sealed interface (Loading / Success / Error)
  navigation/
    AppNavHost.kt           — NavHost, 전체 라우트 등록
    HomeDestination.kt      — object, ROUTE = "home"
    UserDestination.kt      — object, ROUTE = "user/{userId}", createRoute()
  viewmodel/
    UserViewModel.kt        — @HiltViewModel, StateFlow<UserUiState>
  ui/
    screen/
      HomeRoute.kt          — 홈 화면 Composable, DEMO_USER_ID 보유, onUserClick 콜백
      UserRoute.kt          — hiltViewModel() + LaunchedEffect (상태 연결)
      UserScreen.kt         — 순수 Composable (uiState만 받음, 테스트 가능)
    component/
      UserProfile.kt
      LoadingContent.kt
      ErrorContent.kt
```

### 데이터 흐름

```
MainActivity
    ↓ rememberNavController
AppNavHost
    ├─ "home"  →  HomeRoute  →  onUserClick(userId)
    │                                ↓ navController.navigate
    └─ "user/{userId}"  →  DebugUserRoute(userId from backStackEntry)
                                ↓
                           UserRoute (hiltViewModel + LaunchedEffect)
                                ↓
UserRepositoryImpl
    ↓ Result<User>
GetUserUseCase
    ↓ Result<User>
UserViewModel  →  Result.fold()  →  UserUiState
    ↓ StateFlow<UserUiState>
UserScreen  (순수 Composable — 테스트 가능)
```

### 핵심 설계 원칙

- **UserRoute / UserScreen 분리**: Route는 ViewModel과 연결, Screen은 순수 함수 — Composable Preview와 Unit Test 모두 가능
- **UserUiModel 분리**: 도메인 `User` 객체가 UI 레이어에 직접 노출되지 않음
- **Result<T> 반환**: `User?` nullable 대신 `Result<User>`로 에러 표현 명확화
- **UseCase 계층**: ViewModel이 Repository를 직접 호출하지 않음
- **data 레이어 최소 구성**: mock 단계에서는 `repository/` 구현체만 두고, 도메인 모델·인터페이스는 `domain/`에 둠

---

## 4. DI (Hilt) 구조

| 어노테이션 | 위치 | 역할 |
|-----------|------|------|
| `@HiltAndroidApp` | `HiltPracticeApplication` | Hilt 컴포넌트 생성 트리거 |
| `@AndroidEntryPoint` | `MainActivity` | Activity에 Hilt 주입 활성화 |
| `@HiltViewModel` | `UserViewModel` | ViewModel Hilt 주입 |
| `@Module` + `@InstallIn(SingletonComponent)` | `AppModule` | 모듈 등록 |
| `@Binds` | `AppModule` | `domain.repository.UserRepository` → `UserRepositoryImpl` 바인딩 |
| `@Inject constructor` | `UserRepositoryImpl`, `GetUserUseCase` | 생성자 주입 |
| `hiltViewModel()` | `UserRoute` | Composable에서 HiltViewModel 획득 |

```kotlin
// di/AppModule.kt 핵심 패턴
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton  // 앱 전체에서 Repository 인스턴스를 하나만 유지
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
```

---

## 5. 전체 파일 맵

```
Hilt_Practice_Antigravity/
├── build.gradle.kts                         # 루트: 플러그인 선언만
├── gradle/libs.versions.toml               # Version Catalog
├── settings.gradle.kts
├── app/
│   ├── build.gradle.kts                    # compileSdk 36, minSdk 26, kapt
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   └── java/com/cret/hilt_practice/
│       │       ├── HiltPracticeApplication.kt   # @HiltAndroidApp
│       │       ├── MainActivity.kt              # @AndroidEntryPoint
│       │       ├── di/
│       │       │   └── AppModule.kt             # @Module @Binds
│       │       ├── data/
│       │       │   └── repository/
│       │       │       └── UserRepositoryImpl.kt
│       │       ├── domain/
│       │       │   ├── model/
│       │       │   │   ├── User.kt
│       │       │   │   └── UserError.kt         # sealed class
│       │       │   ├── repository/
│       │       │   │   └── UserRepository.kt    # interface
│       │       │   └── usecase/
│       │       │       └── GetUserUseCase.kt
│       │       └── presentation/
│       │           ├── model/
│       │           │   ├── UserUiModel.kt
│       │           │   └── UserUiState.kt       # sealed interface
│       │           ├── navigation/
│       │           │   ├── AppNavHost.kt           # NavHost, 전체 라우트 등록
│       │           │   ├── HomeDestination.kt      # ROUTE = "home"
│       │           │   └── UserDestination.kt      # ROUTE = "user/{userId}"
│       │           ├── viewmodel/
│       │           │   └── UserViewModel.kt     # @HiltViewModel
│       │           └── ui/
│       │               ├── screen/
│       │               │   ├── HomeRoute.kt        # 홈 화면, onUserClick 콜백
│       │               │   ├── UserRoute.kt
│       │               │   └── UserScreen.kt
│       │               ├── component/
│       │               │   ├── UserProfile.kt
│       │               │   ├── LoadingContent.kt
│       │               │   └── ErrorContent.kt
│       │               └── theme/
│       │                   ├── Color.kt
│       │                   ├── Theme.kt
│       │                   └── Type.kt
│       ├── debug/
│       │   └── .../screen/DebugUserRoute.kt    # 상태 컨트롤 바 포함
│       ├── release/
│       │   └── .../screen/DebugUserRoute.kt    # UserRoute 위임만
│       └── test/
│           └── java/com/cret/hilt_practice/
│               ├── util/MainDispatcherRule.kt
│               ├── data/repository/UserRepositoryImplTest.kt  # 3개 테스트
│               ├── domain/usecase/GetUserUseCaseTest.kt       # 2개 테스트
│               └── presentation/viewmodel/UserViewModelTest.kt # 5개 테스트
│           (Android Studio 기본 ExampleUnitTest·ExampleInstrumentedTest는 제거됨)
└── docs/
    ├── screenshots/                         # 앱 스크린샷 5장
    └── retrospective/
        ├── 01_원본_대화_아카이브.md
        ├── 02_QA_요약.md
        ├── 03_개발_지식_수준_분석.md
        └── 04_추가_학습_로드맵.md
```

---

## 6. 주요 에러 처리 패턴

```kotlin
// domain/model/UserError.kt — sealed class
sealed class UserError { ... }

// ViewModel에서 Result.fold 패턴 (실제 UserViewModel.kt)
_uiState.value = getUserUseCase(userId).fold(
    onSuccess = { user -> UserUiState.Success(user.toUiModel()) },
    onFailure = { throwable ->
        if (throwable is CancellationException) throw throwable
        UserUiState.Error(UserError.from(throwable))
    }
)
```

> `UserRepositoryImpl`과 `UserViewModel` 모두 `onFailure`/`catch`에서 `CancellationException`을 rethrow합니다.

---

## 7. Build Variant 전략

| Source Set | `DebugUserRoute.kt` 내용 |
|-----------|--------------------------|
| `debug/`   | 하단 상태 컨트롤 바 포함 (실제/로딩/성공/오류 수동 전환) |
| `release/` | `UserRoute`에 위임만 (`DebugUserRoute` 이름 동일, 내용 다름) |

이 패턴으로 debug 전용 UI를 release 빌드에 완전히 제외합니다.

---

## 8. 단위 테스트 구조

```
test/
├── util/MainDispatcherRule.kt           — StandardTestDispatcher, TestCoroutineScheduler
├── data/repository/UserRepositoryImplTest.kt   — 3개 테스트
│     (성공 Result, 요청 id 반환, User non-null)
├── domain/usecase/GetUserUseCaseTest.kt         — 2개 테스트
│     (성공 위임, 실패 위임)
└── presentation/viewmodel/UserViewModelTest.kt  — 5개 테스트
      (초기 상태 Loading, 성공, 실패 에러 타입, Loading → 완료 전환)
```

**테스트 도구**:
- `MockK` — Repository 목킹
- `kotlinx-coroutines-test` + `StandardTestDispatcher` — 코루틴 테스트
- `MainDispatcherRule` — `Dispatchers.Main` 교체

---

## 9. 개발 히스토리 요약

| 단계 | 내용 | 날짜 |
|------|------|------|
| 1 | Manual DI (`AppContainer` 패턴) 기반 초기 구조 | 2026-04 이전 |
| 2 | Hilt 8개 어노테이션, 컴포넌트 계층, Koin 비교 학습 | 2026-04-12 |
| 3 | Hilt 적용: `AppContainer` 삭제, `AppModule(@Binds)`, `@HiltViewModel` | 2026-04-12 |
| 4 | 코드 리뷰 13개 이슈 해결 (Result<T>, UseCase, UiModel, sealed class 등) | 2026-04 |
| 5 | Debug 상태 컨트롤 바 추가 (build variant source set 분리) | 2026-04 |
| 6 | 단위 테스트 10개 작성 | 2026-04 |
| 7 | Navigation 완성: HomeRoute 신규 + AppNavHost·MainActivity 연결 | 2026-05-28 |
| 8 | domain 레이어 정리: `data/model`·`data/repository` 중복 제거, import 통일 | 2026-05-28 |
| 9 | 미사용 정리: 빈 `data/model` 디렉터리·예제 테스트 제거, PROJECT_CONTEXT 파일 맵 수정 | 2026-05-28 |

**해결한 주요 리팩토링 이슈**:
- `User?` → `Result<User>` 반환 타입 변경
- UseCase 계층 신설 (ViewModel ↔ Repository 직접 의존 제거)
- `UserUiModel` 신설 (도메인 모델 UI 노출 차단)
- `UserRoute` / `UserScreen` 분리 (순수 Composable 테스트 가능)
- `UserError` sealed class (NotFound / Network / Unknown 구분)
- `CancellationException` rethrow 처리
- 접근성 `semantics { contentDescription }` 적용

---

## 10. 개발자 프로필 (AI 지원 맞춤화용)

| 특성 | 내용 |
|------|------|
| **수준** | 중급 Android 개발자 (실용 앱 단독 제작 가능) |
| **강점** | Compose UI, Room, StateFlow, 아키텍처 설계 의식, 문서화 |
| **학습 중** | Hilt 심화, 테스트 코드, 멀티 ViewModel 간 데이터 공유 |
| **학습 스타일** | 실습 중심, 직접 적용하며 배움, AI 도구를 학습 파트너로 활용 |
| **코드 품질 기준** | 단순 동작보다 유지보수 가능한 설계 지향 |
| **선호 언어** | 한국어 |

---

## 11. AI 지원 시 주의사항

> [!NOTE]
> - 이 프로젝트는 **학습 목적**이므로 "왜 이렇게 설계했는지"에 대한 설명을 항상 포함해주세요.
> - 코드 제안 시 **기존 패턴(Result<T>, sealed class, Route/Screen 분리)을 유지**하세요.
> - `CancellationException` rethrow는 이미 적용된 규칙입니다 — 빠뜨리지 마세요.
> - **`@Binds` vs `@Provides` 차이**를 항상 인지하고 적절히 제안하세요.
> - debug/release source set 분리 구조를 유지하세요.

> [!TIP]
> 새 기능 추가 시 체크리스트:
> 1. `domain/model/` — 새 도메인 모델 or Error 타입 필요한가? (`data/`에는 DTO·Entity만)
> 2. `domain/usecase/` — UseCase 계층 거치는가?
> 3. `presentation/model/` — UiModel / UiState 분리되었는가?
> 4. ViewModel → Route → Screen 흐름 유지되는가?
> 5. 단위 테스트 추가했는가?

---

## 12. 다음 학습 방향 (로드맵)

| 우선순위 | 주제 | 현재 상태 |
|---------|------|-----------|
| 🔴 즉시 | Hilt Component Hierarchy 심화 | 어노테이션 사용 가능, 내부 원리 보완 필요 |
| 🔴 즉시 | `@Qualifier` 활용 | 미학습 |
| 🟡 단기 | Navigation 타입 안전 전환 (`@Serializable`) | 문자열 기반 완성, 타입 안전 전환 대상 |
| 🟡 단기 | Kotlin Flow 심화 (`combine`, `SharedFlow`) | `StateFlow`만 사용 중 |
| 🟡 단기 | Compose 심화 (`derivedStateOf`, `SideEffect` 비교) | 기본 사용 중 |
| 🟢 중기 | 멀티모듈 아키텍처 (`:feature`, `:data`, `:domain`) | 단일 `:app` 모듈 |
| 🟢 중기 | CI/CD (GitHub Actions) | 미적용 |

---

*이 문서는 대화 맥락 제공 및 지속적인 엔지니어링 지원을 위해 유지·업데이트됩니다.*
