## 什麼是Spring MVC?
>Spring MVC是一個基於MVC架構的用來簡化web應用程序開發的應用開發框架，它是Spring的一部分，它和Struts2一樣都屬於表現層的框架。
>
>MVC（Model模型View視圖Controller控制器）：這是一種軟件架構思想，是一種開發模式，將軟件劃分為三種不同類型的模塊，分別是模型，視圖，和控制器。
>* 模型：用於封裝業務邏輯處理（java類）
>* 視圖：用於數據展現和操作界面（Servlet）
>* 控制器：用於協調視圖和模型（jsp）
>
>處理流程：視圖將請求發送給控制器，由控制器選擇對應的模型來處理；模型將處理結果交給控制器，控制器選擇合適的視圖來展現處理結果

## 什麼是DispatcherServlet
>引用https://juejin.cn/post/6844903577077415950
>
>與許多其他Web 框架一樣，Spring MVC 同樣圍繞前端頁面的控制器模式(Controller) 進行設計，其中最為核心的Servlet——DispatcherServlet為來自客戶端的請求處理提供通用的方法，而實際的工作交由可自定義配置的組件來執行。
>
>和任何普通的Servlet 一樣，DispatcherServlet 需要根據Servlet規範使用Java代碼配置或在web.xml 文件中聲明請求和Servlet的映射關係。DispatcherServlet通過讀取Spring的配置來發現它在請求映射，視圖解析，異常處理等方面所依賴的組件。

**以下是在web.xml 中註冊和初始化DispatcherServlet 的方法**
```
<!-- Spring MVC Configs -->
 
<!-- Step 1: Configure Spring MVC Dispatcher Servlet -->
<servlet>
	<servlet-name>dispatcher</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/spring-mvc-demo-servlet.xml</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
 
<!-- Step 2: Set up URL mapping for Spring MVC Dispatcher Servlet -->
<servlet-mapping>
	<servlet-name>dispatcher</servlet-name>
	<url-pattern>/</url-pattern>
</servlet-mapping>
```
**以下是在spring-mvc-demo-servlet.xml中配置組件掃描，添加annotation-driven以及視圖解析**
```
<!-- Step 3: Add support for component scanning -->
<context:component-scan base-package="com.luv2code.springdemo" />

<!-- Step 4: Add support for conversion, formatting and validation support -->
<mvc:annotation-driven/>

<!-- Step 5: Define Spring MVC view resolver -->
<bean
	class="org.springframework.web.servlet.view.InternalResourceViewResolver">
	<property name="prefix" value="/WEB-INF/view/" />
	<property name="suffix" value=".jsp" />
</bean>
	
<!-- Load custom message resources -->
<bean id="messageSource"
	class="org.springframework.context.support.ResourceBundleMessageSource">
	<property name="basenames" value="resources/messages" />
</bean>
```
## Spring MVC實作以及相關註解使用
>引用https://segmentfault.com/a/1190000005670764
>
>在SpringMVC 中，控制器Controller負責處理由DispatcherServlet分發的請求，它把用戶請求的數據經過業務處理層處理之後封裝成一個Model，然後再把該Model返回給對應的View進行展示。
>
>在SpringMVC中提供了一個非常簡便的定義Controller的方法，你無需繼承特定的類或實現特定的接口，只需使用@Controller標記一個類是Controller，然後使用@RequestMapping和@RequestParam 等一些註解用以定義URL 請求和Controller方法之間的映射，這樣的Controller就能被外界訪問。
>
>* `@Controller`：用於標記在一個類上，使用它標記的類就是一個SpringMVC Controller對象。分發處理器將會掃描使用了該註解的類的方法，並檢測該方法是否使用了@RequestMapping 註解。
>* `@RequestMapping`：這個註解會將HTTP請求映射到MVC和REST控制器的處理方法上。標記在Controller類上的時候，裡面使用@RequestMapping標記的方法的請求地址都是相對於類上的@RequestMapping而言的；當Controller類上沒有標記@RequestMapping註解時，方法上的@RequestMapping都是絕對路徑。這種絕對路徑和相對路徑所組合成的最終路徑都是相對於根路徑“/ ”而言的。
>* `HttpServletRequest`：HttpServletRequest對象代表客戶端的請求，當客戶端通過HTTP協議訪問服務器時，HTTP請求頭中的所有信息都封裝在這個對象中，通過這個對象提供的方法，可以獲得客戶端請求的所有信息。

**以下範例包含映射處理、JSP視圖轉向顯示、HttpServletRequest數據獲取以及Model添加數據等操作**
```
@Controller
@RequestMapping("/hello")
public class HelloWorldController {

	// need a controller method to show the initial HTML form
	
	@RequestMapping("/showForm")
	public String showForm()
	{
		return "helloworld-form";
	}
	
	// need a controller method to process the HTML form
	@RequestMapping("/processForm")	
	public String processForm()
	{
		return "helloworld";
	}
	
	// new a controller method to read from data and add data to the model
	@RequestMapping("/processFormVersionTwo")
	public String letsShoutDude(HttpServletRequest request, Model model)
	{
		// read the request parameter form the HTML form
		String theName = request.getParameter("studentName");
		
		// convert the data to all caps
		theName = theName.toUpperCase();
		
		// create the message
		String result = "Yo! " + theName;
		
		// add message to the model
		model.addAttribute("message", result);
		
		return "helloworld";
	}
	
	@RequestMapping("/processFormVersionThree")
	public String processFormVersionThree(@RequestParam("studentName") String theName,
			Model model)
	{
		// convert the data to all caps
		theName = theName.toUpperCase();
		
		// create the message
		String result = "Hey My Friend from v3! " + theName;
		
		// add message to the model
		model.addAttribute("message", result);
		
		return "helloworld";
	}
}
```
**helloworld-form.jsp**
```
<!DOCTYPE html>
<html>

<head>
	<title>Hello World - Input Form</title>
</head>

<body>

	<form action="processFormVersionThree" method="GET">
		
		<input type="text" name="studentName"
			placeholder="What's your name?" />
			
			<input type="submit" />
	</form>

</body>

</html>
```
**helloworld.jsp**
```
<!DOCTYPE html>
<html>

<body>
Hello World of Spring!

<br><br>

Student Name: ${param.studentName}

<br><br>

The message: ${message}

</body>

</html>
```

## 表單標籤庫與數據綁定
**創建學生類別**
```
public class Student {

	private String firstName;
	private String lastName;
		
	public Student(){
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
```
**@ModelAttribute是一個註解，將方法參數或方法返回值綁定到命名的模型屬性，然後將其暴露給 Web 視圖。**
```
@Controller
@RequestMapping("/student")
public class StudentController {

	// Step.1 創建類別物件
	@RequestMapping("/showForm")
	public String showForm(Model theModel)
	{
		// create a student object
		Student theStudent = new Student();
		
		// add student object to model 
		theModel.addAttribute("student", theStudent);
		
		// Step.2 創建student-form頁面
		return "student-form";
	}
	
	// Step.3 創建表單送出動作，回傳設定之值並填入變數中存取
	@RequestMapping("/processForm")
	public String processForm(@ModelAttribute("student") Student theStudent)
	{
		// log the input data
		System.out.println("theStudent: " + theStudent.getFirstName()
							+ " " + theStudent.getLastName());
		
		// Step.4 創建student-confirmation頁面，接收回傳之值並顯示於頁面中
		return "student-confirmation";
	}
}
```
**student-form.jsp**
```
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>

<html>

<head>

<title>Student Registration Form</title>

</head>

<body>

	<form:form action="processForm" modelAttribute="student">
	
	First name:<form:input path="firstName" />
	
	<br><br>
	
	Last name:<form:input path="LastName" />
	
	<input type="submit" value="Submit" />
	
	</form:form>

</body>

</html>
```
**student-confirmation.jsp**
```
<!DOCTYPE html>

<html>

<head>

<title>Student Confirmation</title>

</head>

<body>

The student is confirmation: ${student.firstName} ${student.lastName}

</body>

</html>
```
>表單標籤庫中包含了可以用在JSP 頁面中渲染HTML元素的標籤。在JSP 頁面使用Spring 表單標籤庫時，必須在JSP 頁面開頭處聲明taglib指令，
>
>在表單標籤庫中有form、input、password、hidden、textarea、checkbox、checkboxes、radiobuttton、radiobuttons、select、option、options、errors 等標籤。
>
>表單標籤除了具有HTML 表單元素屬性以外，還具有acceptCharset、commandName、cssClass、cssStyle、htmlEscape 和modelAttribute 等屬性。

**以下範例包含`option`、`radiobuttton`、`checkbox`演示**
```
Country:
	
<form:select path="country">
	
	<form:options items="${student.countryOptions}" />
		
</form:select>
	
<br><br>
	
<form:radiobuttons path="favoriteLanguage" items="${student.favoriteLanguageOptions}"  />
	
<br><br>
	
Operating System:
	
Linux <form:checkbox path="operatingSystems" value="Linux" />
Mac OS <form:checkbox path="operatingSystems" value="Mac OS" />
MS Windows <form:checkbox path="operatingSystems" value="MS Windows" />
```

## Spring MVC表單驗證
>引用https://www.jianshu.com/p/0bfe2318814f
>
>Hibernate Validator是Bean Validation 的參考實現. HibernateValidator提供了JSR303規範中所有內置constraint的實現，除此之外還有一些附加的constraint。
>
>Bean Validation為JavaBean驗證定義了相應的元數據模型和API。缺省的元數據是Java Annotations，通過使用XML可以對原有的元數據信息進行覆蓋和擴展。BeanValidation是一個運行時的數據驗證框架，在驗證之後驗證的錯誤信息會被馬上返回。
>
>**Hibernate Validator 的作用**
>* 驗證邏輯與業務邏輯之間進行了分離，降低了程序耦合度
>* 統一且規範的驗證方式，無需你再次編寫重複的驗證代碼
>* 你將更專注於你的業務，將這些繁瑣的事情統統丟在一邊
>
```
public class Customer {

	private String firstName;
	
	@NotNull(message="is required")
	@Size(min=1, message="is required")
	private String lastName;
	
	@Pattern(regexp="^[a-zA-Z0-9]{5}", message="only 5 chars/digits")
	private String postalCode;
	
	@CourseCode(value="TOPS", message="must start with TOPS")
	private String courseCode;
	
	@NotNull(message="is required")
	@Min(value=0, message="must be greater than or equal to zero")
	@Max(value=10, message="must be less than or equal to 10")
	private Integer freePasses;		
	
	// Getter...
	// Setter...
}
```
>`@Valid`的參數後必須緊跟著一個BindingResult參數，否則spring會在校驗不通過時直接拋出異常
>
>在執行 Spring MVC 驗證時，`BindingResult`參數的位置非常重要。在方法簽名中，BindingResult參數必須緊跟在模型屬性之後。如果將它放在任何其他位置，SpringMVC驗證將無法按預期工作。事實上，您的驗證規則將被忽略
>
>`@InitBinder`從字面意思可以看出這個的作用是給Binder做初始化的，@InitBinder主要用在@Controller中標註於方法上（@RestController也算），表示初始化當前控制器的數據綁定器（或者屬性綁定器），只對當前的Controller有效。@InitBinder標註的方法必須有一個參數`WebDataBinder`。所謂的屬性編輯器可以理解就是幫助我們完成參數绑定，然後是在請求到達controller要執行方法前執行！
>
>WebDataBinder的作用是從Web 請求中，把請求裡的參數都綁定到對應的JavaBean上！在Controller方法中的參數類型可以是基本類型，也可以是封裝後的普通Java類型。若這個普通的Java類型沒有聲明任何註解，則意味著它的每一個屬性都需要到Request中去查找對應的請求參數，而WebDataBinder則可以帮助我們實現從Request中取出請求參數並綁定到JavaBean中。
```
@Controller
@RequestMapping("/customer")
public class CustomerController {

	// add an initbinder ... to convert trim input strings
	// remove leading and trailing whitespace
	// resolve issue for our validation
	@InitBinder
	public void initBinder(WebDataBinder dataBinder)
	{
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@RequestMapping("/showForm")
	public String showForm(Model theModel)
	{
		theModel.addAttribute("customer", new Customer());
		
		return "customer-form";
	}
	
	// @Valid Perform validation rules on Customer object 
	// BindingResult Results of validation placed in the BindingResult 
	@RequestMapping("/processForm")
	public String processForm(
			@Valid @ModelAttribute("customer") Customer theCustomer,
			BindingResult theBindingResult)
	{
		// 檢查BindingResult是否有錯誤
		// 有錯誤 => 返回客戶表單
		if(theBindingResult.hasErrors()) 
			{
				return "customer-form";
			}
		// 無錯誤 => 返回客戶確認結果(成功頁面)
		else 
			{
				return "customer-confirmation"; 			
			}
	}
}
```
**在messages.properties中加入自定義錯誤訊息**
```
// 錯誤型態.類別.屬性名稱 = 自定義錯誤訊息
typeMismatch.customer.freePasses=Invalid number
```
**在spring-mvc-demo-servlet.xml中添加自定義來源路徑**
```
<!-- Load custom message resources -->
<bean id="messageSource"
	class="org.springframework.context.support.ResourceBundleMessageSource">
	<property name="basenames" value="resources/messages" />
</bean>
```
>**自定義註解**
>
>參考https://blog.csdn.net/qq_36747237/article/details/102577720
>
>`@Constraint`：給出具有業務規則的實際類，用於驗證此過程
>
>`@Target`：給出此註解可運用於何處
>
>`@Retention`：表示註解保留時間長短
```
@Constraint(validatedBy = CourseCodeConstraintValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface CourseCode {

	// define default course code
	public String value() default "LUV";
	
	// define default error message
	public String message() default "must start with LUV";
	
	// define default groups
	public Class<?>[] groups() default {};
	
	// define default payloads
	public Class<? extends Payload>[] payload() default {};
}
```
**實現CourseCodeConstraintValidator驗證方法**
```
public class CourseCodeConstraintValidator implements ConstraintValidator<CourseCode, String>{

	private String coursePrefix;
	
	@Override
	public void initialize(CourseCode constraintAnnotation) {
		coursePrefix = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(String theCode, ConstraintValidatorContext theConstraintValidatorContext) {

		// Validation logic
		// Test if the form data starts with our course prefix
		// Does it start with "LUV"?
		boolean result;
		
		if (theCode != null)
		{						
			result =theCode.startsWith(coursePrefix);
		}
		else result = true;
		
		return result;
	}
}
```
