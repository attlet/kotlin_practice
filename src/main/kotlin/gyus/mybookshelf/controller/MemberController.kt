package gyus.mybookshelf.controller

import gyus.mybookshelf.model.BaseMember
import gyus.mybookshelf.model.Member
import gyus.mybookshelf.repository.MemberRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/member")
@RestController
class MemberController(private val memberRepository: MemberRepository) {

    @GetMapping("/init")
    fun initMember(){
        val members = mutableListOf<Member>()
        for(i in 1 .. 100){
            val name = "member$i"
            val member = Member( name = name, email = "$name@email.com")
            members.add(member)
        }

        memberRepository.saveAll(members)

        val andy1 = Member("andy", email = "andy1@mybookshelf.com")
        val andy2 = Member("andy", email = "andy2@mybookshelf.com")
        val andy3 = Member("andy", email = "andy3@mybookshelf.com")
        memberRepository.save(andy1)
        memberRepository.save(andy2)
        memberRepository.save(andy3)
    }
    @GetMapping("/createMember")
    fun createMember(
        @RequestParam("name") name: String,
        @RequestParam("email") email: String
    ):BaseMember{
        val member = Member(name, email);
        memberRepository.save(member)   //기본적인 jpa에서 제공되는 저장 메서드
        return member
    }

    @GetMapping("/allMembers")
    fun allMembers(): List<BaseMember> {
        return memberRepository.findAll()
    }

    @GetMapping("/findByName")
    fun findByName(
        @RequestParam("name") name: String
    ) : BaseMember? {
        return memberRepository.findByname(name)  //쿼리 메서드 사용
    }

    @GetMapping("/findByEmailContaining")
    fun findByEmailContaining(
        @RequestParam("email") email: String
    ) : List<BaseMember>{
        return memberRepository.findByEmailContaining(email)
    }

    @GetMapping("/countDistinctByName")
    fun countDistinctByName(
        @RequestParam("email") email:String
    ) : Int {
        return memberRepository.countDistinctByName(email)
    }
}