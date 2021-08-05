# SECURING-WEB

使用Spring Boot + Spring Security实现用户名密码登录认证和token认证，并将日志写入数据库。

## Spring Security框架介绍
Spring Security的核心是一组过滤器链，用户请求需要通过这些过滤器才能访问网络资源。其认证流程如下

```flow
st=>start: 用户请求
op1=>operation: 进入对应的过滤器
con1=>condition: 是否进行认证
op2=>operation: 提取AuthenticationToken
op3=>operation: AuthenticationManager
op4=>operation: AuthenticationProvider
con2=>condition: 是否认证成功
op5=>operation: AuthenticationFailureHandler
op6=>operation: AuthenticationSuccessHandler
e=>end: 进入后续服务

st->op1->con1
con1(yes)->op2->op3->op4->con2
con2(yes)->op6->e
con2(no)->op5->e
con1(no)->e
```

&emsp;
AuthenticationToken: 包含认证所需的信息（如用户名、密码）。所有提交给AuthenticationManager的认证请求需要被封装成一个Token的实现。 
AuthenticationManager: 用户认证的管理类，所有的认证请求都会通过`authenticate()`方法来实现。而具体校验动作会由AuthenticationManager将请求转发给具体的实现类AuthenticationProvider来完成。 
AuthenticationProvider: 认证的具体实现类，完成对用户的认证。

## 用户名密码登录
`MyUserPasswordAuthFilter`过滤器拦截登录请求。Provider使用Spring Security提供的一个默认实现的`DaoAuthenticationProvider`，其中调用了`UserDetailsService`来获取用户信息。用户信息以`UserDetails`格式返回。
这里使用`JwtUserService`实现`UserDetailsService`，并添加了生成token的方法。另外，密码需要进行加密编码处理`passwordEncoder.encode("password")`
`LoginSuccessHandler`中完成token的签发，`LoginFailureHandler`直接返回401(UNAUTHORIZED)
`LoginConfig`实现对`MyUserPasswordAuthFilter`的配置。
最后在`WebSecurityConfig`中应用Filter。

## Token认证
使用JWT作为token进行用户身份认证。 
JWT(Json Web Token)是一个开放标准(RFC 7519)，它定义了一种紧凑的、自包含的方式，用于作为JSON对象在各方之间安全地传输信息。该信息可以被验证和信任，因为它是数字签名的。 
JWT由三部分组成，分别是header, payload和signature，它们之间用圆点(.)连接。
使用`com.auth0.jwt`库可以实现JWT的签发与认证。

`JwtAuthFilter`是用于认证JWT的过滤器。从`OncePerRequestFilter`继承，覆写`doFilterInternal()`方法实现过滤器主要功能。从http请求中获取token并将其装入`JwtAuthenticationToken`，然后传入给AuthenticationManager的`authenticate()`进行认证。结果会以`Authentication`的形式返回。根据返回结果执行`JwtAuthSuccessHandler`或`JwtAuthFailureHandler`。
另外在filter中实现了允许URL通过的方法，访问特定URL可直接通过过滤器，不用进行JWT认证。
`JwtLoginConfig`实现`JwtAuthFilter`配置。
最后同样在`WebSecurityConfig`中应用Filter。

## 日志写入数据库
`LogInfoService`提供读取和写入数据库服务。`LogInfo`中成员对应数据库中的信息。`LogInfoDao`继承`JdbcDaoSupport`，这是Spring提供了一个类，用于简化DAO的实现，其核心代码就是持有一个`JdbcTemplate`。`JdbcTemplate`中的`update()`方法用于插入、更新和删除操作，`query()`方法用于查询，可以返回多条记录。
在`LogInfo`需要有对应的set成员变量的方法，否则查询结果无法正确以`LogInfo`形式返回。
在`application.properties`写入数据库信息，实现与数据库的连接。

## 其他
`WebSecurityConfig`中`@EnableWebSecurity`表示启用Spring Security。其中的`formLogin()`是Spring Security自带的表单登录，可以实现用户名密码登录，使用session记录用户状态。`logout()`注销，会清除用户状态。

<b>参考</b>
<https://www.jianshu.com/p/d5ce890c67f7>