package gyus.mybookshelf.controller

import gyus.mybookshelf.model.Post
import gyus.mybookshelf.model.PostDTO
import gyus.mybookshelf.service.PostService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/post")
class PostController (private val postService: PostService){

    @GetMapping("/all")
    fun allPosts(): List<Post>{
        return postService.findAllPost()
//        return postService.findAllWithMember() //n + 1문제 해결한 메서드
    }

    @GetMapping("/search")
    fun findPostsWithTitleList(
        @RequestParam("title")title :String
    ):List<Post>{
        return postService.findPostsWithTitleList(title)
    }

    @GetMapping("/title")
    fun findPostSelectTitle():List<String>{
        return postService.findPostsSelectTitle()
    }

    @GetMapping("/paging")
    fun findPostPagingWithSort(@RequestParam("page") page:Int=1,
                               @RequestParam("size") size:Int=10) :List<PostDTO>{
        val pageable = PageRequest.of(page-1, size)
        return postService.findPostPagingWithSort(pageable)
    }

    @GetMapping("/condition")
    fun findPostByCondition(
        @RequestParam("condition") condition:String,
        @RequestParam("value") value:String,
        @RequestParam("page") page:Int=1,
        @RequestParam("size") size:Int=10
    ) :List<PostDTO> {
        val pageable = PageRequest.of(page-1,size)
        return postService.findPostByCondition(condition,value,pageable)

    }
}