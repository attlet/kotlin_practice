package gyus.mybookshelf.service

import gyus.mybookshelf.model.BaseMember
import gyus.mybookshelf.model.Episode
import gyus.mybookshelf.model.Post
import gyus.mybookshelf.model.PostDTO
import gyus.mybookshelf.repository.EpisodeRepository
import gyus.mybookshelf.repository.PostRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

@Service
class PostService(
    private val postRepository: PostRepository,
    private val episodeRepository: EpisodeRepository
) {
    //propagation을 통해 트랜잭션 전파 속성 지정 가능
    //예로 never로 지정시 트랜잭션 타지 않도록함.
    //isolation을 통해 트랜잭션 격리 수준 지정 가능
    //serialize를 하면 안전하나 성능이 안 나와 잘 사용x 격리 수준 기본값은 db마다 다름.
    @Transactional(propagation = Propagation.NEVER, isolation = Isolation.READ_COMMITTED)  //격리수준은 디비 격리수준에 의존.
    fun writePost(member:BaseMember, title:String, content:String, isEpisode:Boolean){
        val post = Post(title = title, content=content, member=member, isEpisode = isEpisode)
        postRepository.save(post)
        throw RuntimeException("트랜잭션 테스트")
        if(isEpisode){
            val episode = Episode(post=post)
            episodeRepository.save(episode)
        }
    }

    fun postCount():Long {
        return postRepository.count()
    }

    fun episodeCount():Long {
        return episodeRepository.count()
    }

    //n + 1 문제를 확인해보기 위한 메서드
    //이러고 DataInit에서 데이터 post save 하는 작업에서 post하나 select할 때마다
    //member 20개 select하는 현상 발생
    //EntityGraph어노테이션을 이용해서 해결 가능
    @Transactional(readOnly = true)
    fun findAllPost() : List<Post>{
        return postRepository.findAll()
    }

    //fetch join을 이용해 n + 1 문제를 해결
    @Transactional(readOnly = true)
    fun findAllWithMember():List<Post>{
        return postRepository.findAllWithMember()
    }

    fun findPostsWithTitleList(title:String):List<Post>{
        return postRepository.findPostsWithTitleList(title)
    }

    fun findPostsSelectTitle():List<String>{
        return postRepository.findPostsSelectTitle()
    }

    fun findPostPagingWithSort(pageable: Pageable):List<PostDTO>{
        return postRepository.findPostsPagingWithSort(pageable)
    }

    fun findPostByCondition(condition:String, value :String ,pageable: Pageable) :List<PostDTO>{
        return postRepository.findPostByCondition(condition,value,pageable)
    }
}