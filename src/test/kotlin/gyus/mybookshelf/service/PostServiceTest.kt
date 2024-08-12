package gyus.mybookshelf.service

import gyus.mybookshelf.model.Member
import gyus.mybookshelf.repository.MemberRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.RuntimeException

@SpringBootTest
class PostServiceTest {

    @Autowired
    lateinit var postService:PostService
    @Autowired
    lateinit var memberRepository: MemberRepository
    @Test
    fun writePost() {
        val member = Member(name="andy", email="andy@email.com")
        memberRepository.save(member)

        //writePost안에 두 개의 작업.
        //하나는 post save, 하나는 episode save
        //임의로 두 작업 사이에 runtime exception 발생하도록 하고, 테스트
        assertThrows<RuntimeException>{
            postService.writePost(member, title = "title", content="content", isEpisode = true)
        }

        val postCount = postService.postCount()
        val episodeCount = postService.episodeCount()

        //두 개가 다르면 트랜잭션 동작이 안 된것
        //중간에 롤백 시 이전 작업도 한 트랜잭션에 속해있다면 롤백되어야함.
        println("post count : $postCount, episode count : $episodeCount")

    }

}