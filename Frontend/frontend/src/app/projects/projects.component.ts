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
  currentUserRole: string = this.authService.getUser()?.role;
  currentStatus: 'APPROVED' | 'REJECTED' | 'PENDING_APPROVAL' | null = null;

  constructor(private postService: PostService, private authService: AuthService) { }

  ngOnInit(): void {
    this.postService.getAllPosts().subscribe(posts => {
      if (!this.isAuthorized() || this.currentUserRole === 'USER') {
        this.posts = posts.filter(post => post.status === 'APPROVED');
      } else {
        this.posts = posts;
      }
    });
    this.imageStr = 'http://localhost:8080'
  }

  isAuthorized(): boolean {
    return !!this.currentUserRole;
  }
  isLiked(post: Post): boolean {
    // Check if the current user has liked this post
    return post.likes.some(like => like.user.id === this.currentUserId);
  }

  toggleLike(postId: number, userId: number): void {
    this.postService.toggleLike(postId, userId).subscribe(() => {
      this.loadPosts();
    });
  }

  canSeeStatus(): boolean {
    return this.currentUserRole === 'ADMIN' || this.currentUserRole === 'MODERATOR';
  }

  approvePost(postId: number): void {
    this.postService.approvePost(postId).subscribe(() => {
      this.loadPosts();
    });
  }

  rejectPost(postId: number): void {
    this.postService.rejectPost(postId).subscribe(() => {
      this.loadPosts();
    });
  }

  // Update this method to get posts based on the current status
  loadPosts(): void {
    if (this.currentStatus) {
      this.postService.getPostsByStatus(this.currentStatus).subscribe(posts => {
        this.posts = posts;
      });
    } else {
      this.postService.getAllPosts().subscribe(posts => {
        this.posts = posts;
      });
    }
  }

  // Add a method to change the current status and reload the posts
  filterByStatus(status: 'APPROVED' | 'REJECTED' | 'PENDING_APPROVAL' | null): void {
    this.currentStatus = status;
    this.loadPosts();
  }
}
