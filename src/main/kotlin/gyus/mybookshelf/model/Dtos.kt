package gyus.mybookshelf.model

import jakarta.persistence.*
import java.time.Instant

class MemberDTO(val name:String, val email:String) {
    companion object{
        fun toDto(entity:BaseMember):MemberDTO{
            return MemberDTO(entity.name, entity.email)
        }
    }
}

class PostDTO(
    val id: Long = 0,   //다른 클래스가 상속을 하는
    val title:String,
    val content:String,
    val isEpisode:Boolean = false,
    val memberName:String? = null,
    val createdDt: Instant = Instant.now(),
    val updateDt: Instant = Instant.now()
){
    constructor(
        id: Long = 0,   //다른 클래스가 상속을 하는
        title:String,
        content:String,
        isEpisode:Boolean = false,
        member:BaseMember,
        createdDt: Instant = Instant.now(),
        updateDt: Instant = Instant.now()
    ) : this(id, title, content, isEpisode, memberName=member.name,createdDt, updateDt)

}