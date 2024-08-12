package gyus.mybookshelf.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import gyus.mybookshelf.model.Post
import gyus.mybookshelf.model.PostDTO
import gyus.mybookshelf.model.QPost
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport


interface PostRepository: JpaRepository<Post, Long>, PostRepositoryCustom{

    @Query("select p from Post p JOIN FETCH p.member")
    fun findAllWithMember(): List<Post>

    @EntityGraph(attributePaths = ["member"])
    override fun findAll():List<Post>

}

interface PostRepositoryCustom{
    fun findPostsWithTitleList(title:String):List<Post>
    fun findPostsSelectTitle():List<String>
    fun findPostsPagingWithSort(pageable: Pageable):List<PostDTO>
    fun findPostByCondition(condition:String, value:String, pageable: Pageable) :List<PostDTO>
}

class PostRepositoryImpl:PostRepositoryCustom, QuerydslRepositorySupport(Post::class.java){
    private val post = QPost.post
    override fun findPostsWithTitleList(title: String): List<Post> {
        return from(post)
            .where(post.title.contains(title))
            .fetch()
    }

    override fun findPostsSelectTitle(): List<String> {
        return from(post)
            .select(post.title)
            .fetch()
    }

    override fun findPostsPagingWithSort(pageable: Pageable):List<PostDTO> {
        return from(post)
            .select(
                Projections.constructor(
                    PostDTO::class.java,
                    post.id,
                    post.title,
                    post.content,
                    post.isEpisode,
                    post.member,
                    post.createdDt,
                    post.updateDt
                )
            )
            .orderBy(post.createdDt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }

    override fun findPostByCondition(condition: String, value: String, pageable: Pageable): List<PostDTO> {
        val builder = BooleanBuilder()

        when(condition){
            "id" -> builder.and(post.id.eq(value.toLong()))
            "title" -> builder.and(post.title.contains(value))

        }

        return from(post)
            .select(
                Projections.constructor(
                    PostDTO::class.java,
                    post.id,
                    post.title,
                    post.content,
                    post.isEpisode,
                    post.member,
                    post.createdDt,
                    post.updateDt
                )
            )
            .where(builder)
            .orderBy(post.createdDt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }
}