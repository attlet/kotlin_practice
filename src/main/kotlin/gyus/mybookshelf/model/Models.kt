package gyus.mybookshelf.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "member")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  //단일 테이블 구분 컬럼으로 상속
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING) //DiscriminaotorType.String 은 기본값
abstract class BaseMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,   //다른 클래스가 상속을 하는 것을 허용하려면 open키워드를 사용해야함
    @Column(length = 20)
    open val name: String,
    @Column(unique = true)
    open val email: String,
    open val createDt: Instant = Instant.now(),
    open val updateDt: Instant = Instant.now()
) {
    override fun toString(): String {
//        println(this::class.simpleName) //클래스 이름 출력
        return "User(id : $id, name : $name, email : $email, $createDt, $updateDt)"
    }

    fun fromDto(dto:MemberDTO):BaseMember{
        return when (this) {
            is Member -> Member(dto.name, dto.email)
            is Author -> Author(dto.name, dto.email)
            else -> throw NotImplementedError("멤버 클래스가 아닙니다.")
        }
    }
}

@Entity
@DiscriminatorValue("NORMAL")
class Member(
    name: String,
    email: String,
) : BaseMember(name = name, email = email)

@Entity
@DiscriminatorValue("AUTHOR")
class Author(
    name: String,
    email: String,
) : BaseMember(name = name, email = email)
