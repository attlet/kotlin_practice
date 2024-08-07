package gyus.mybookshelf.model

class MemberDTO(val name:String, val email:String) {
    companion object{
        fun toDto(entity:BaseMember):MemberDTO{
            return MemberDTO(entity.name, entity.email)
        }
    }
}