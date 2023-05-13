import { Component, OnInit } from '@angular/core';
import {Post, PostService} from "../services/post.service";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-project',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit {
  posts: Post[] = [];
  imageStr: string  = ''
  currentUserId: number = this.authService.getUser()?.id;

  constructor(private postService: PostService, private authService: AuthService) { }

  ngOnInit(): void {
    this.postService.getAllPosts().subscribe(posts => {
      this.posts = posts;
    });
    this.imageStr = 'http://localhost:8080'
  }

  isLiked(post: Post): boolean {
    // Check if the current user has liked this post
    return post.likes.some(like => like.user.id === this.currentUserId);
  }

  toggleLike(postId: number, userId: number): void {
    this.postService.toggleLike(postId, userId).subscribe(() => {
      this.ngOnInit();
    });
  }

}
