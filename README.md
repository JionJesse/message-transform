介绍：
json报文由如下注解完成：
JsonPath:只有一个属性value,也是默认属性,用于描述此对象在json字符串中的key
BeanClass：只有一个属性value,也是默认属性，代表子对象,用于描述对象中含有子对象的情况

示例：
1、简单对象
报文字符串：
{
    "carName": "凯美瑞", 
    "carId": "S0001"
}
格式化类配置：
public class Car {
    @JsonPath("carId")
    public String carId;
    @JsonPath("carName")
    public String carName;
}

2、多对象
报文字符串：
{
    "CAR": {
        "carName": "凯美瑞", 
        "carId": "S0001"
    }, 
    "HOUSE": {
        "address": "江苏", 
        "size": 100
    }
}
格式化类配置：
public class MultiObject {
    @BeanClass(Car.class)
    public Car car;
    
    @BeanClass(House.class)
    public House house;
}

@JsonPath("CAR")
public class Car {
    @JsonPath("carId")
    public String carId;
    @JsonPath("carName")
    public String carName;
}

@JsonPath("HOUSE")
public class House {
    @JsonPath("address")
    public String address;
    @JsonPath("size")
    public Integer size;
}

3、集合
报文字符串：
{
    "LIST": [
        {
            "carName": "凯美瑞", 
            "carId": "S0001"
        }, 
        {
            "carName": "凯美瑞2", 
            "carId": "S0002"
        }
    ]
}

格式化类配置：
public class ListObject {  
    @JsonPath("LIST")
    @BeanClass(Car.class)
    public List<Car> list;
}

@JsonPath("CAR")
public class Car {
    @JsonPath("carId")
    public String carId;
    @JsonPath("carName")
    public String carName;
}

4、对象和集合
报文字符串：
{
    "HOUSE": {
        "address": "江苏", 
        "size": 100
    }, 
    "LIST": [
        {
            "carName": "凯美瑞", 
            "carId": "S0001"
        }, 
        {
            "carName": "凯美瑞2", 
            "carId": "S0002"
        }
    ]
}
格式化类配置：
public class ListObject {
    @BeanClass(House.class)
    public House house;
    
    @JsonPath("LIST")
    @BeanClass(Car.class)
    public List<Car> list;
}

@JsonPath("HOUSE")
public class House {
    @JsonPath("address")
    public String address;
    @JsonPath("size")
    public Integer size;
}

@JsonPath("CAR")
public class Car {
    @JsonPath("carId")
    public String carId;
    @JsonPath("carName")
    public String carName;
}

5、混合类型
报文字符串：
{
    "chirlds": [
        {
            "name": "json1", 
            "house": {
                "size": 11, 
                "id": "cTP001"
            }, 
            "age": 1
        }, 
        {
            "cars": [
                {
                    "carName": "凯美瑞", 
                    "carId": "DDDDD2"
                }
            ], 
            "name": "json2", 
            "house": {
                "size": 2, 
                "id": "TP002"
            }, 
            "age": 2
        }
    ], 
    "phone": 1328974033, 
    "name": "周杰伦", 
    "houses": [
        {
            "size": 3, 
            "id": "TP003"
        }
    ], 
    "age": 37, 
    "info": {
        "jobNo": "014397", 
        "house": {
            "size": 4, 
            "id": "TP004"
        }
    }
}
格式化类配置：
public class Persion {
    @JsonPath("name")
    public String name;
    
    @JsonPath("age")
    public Integer age;
    
    @JsonPath("phone")
    public Integer phone;
    
    @BeanClass(Info.class)
    public Info info = new Info();
    
       @JsonPath("houses")
    @BeanClass(House.class)
    public List<House> houses;
    
    @JsonPath("chirlds")
    @BeanClass(Chirld.class)
    public List<Chirld> chirlds = new ArrayList<Chirld>(); 
}

@JsonPath("info")
public class Info {
    @JsonPath(value="jobNo")
    public String jobNo;
    @BeanClass(House.class)
    public House house = new House();   
}

@JsonPath("house")
public class House {   
    @JsonPath("id")
    public String id;
    
    @JsonPath(value="size")
    public Integer size;  
}

@JsonPath(value="chirld")
public class Chirld {
    
    @JsonPath(value="name")
    public String name;
    
    @JsonPath(value="age")
    public BigDecimal age;
    
    @JsonPath(value="cars")
    @BeanClass(Car.class)
    public List<Car> cars;
    @BeanClass(House.class)
    public House house;   
}

@JsonPath("car")
public class Car {
    @JsonPath("carId")
    public String carId;
    @JsonPath("carName")
    public String carName;    
}
