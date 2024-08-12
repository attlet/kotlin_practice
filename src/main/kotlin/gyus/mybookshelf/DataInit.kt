package gyus.mybookshelf

import gyus.mybookshelf.model.Member
import gyus.mybookshelf.model.Post
import gyus.mybookshelf.repository.MemberRepository
import gyus.mybookshelf.repository.PostRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInit(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository
) : CommandLineRunner{
    override fun run(vararg args: String?) {
        //Post 20개 추가
        //member도 그에 따라 추가

        val posts = mutableListOf<Post>()
        for(i in 1 .. 20){
            val member = Member(name = "member$i", email="member$i@email.com")
            memberRepository.save(member)
            val post = Post(title = "title$i", content = "content$i", member = member, isEpisode = true)
            posts.add(post)
        }
        postRepository.saveAll(posts)


    }


}