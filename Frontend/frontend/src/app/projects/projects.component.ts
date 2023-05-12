import { Component, OnInit } from '@angular/core';
import {Post, PostService} from "../services/post.service";

@Component({
  selector: 'app-project',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit {
  posts: Post[] = [];
  imageStr: string  = ''
  constructor(private postService: PostService) { }

  ngOnInit(): void {
    this.postService.getAllPosts().subscribe(posts => {
      this.posts = posts;
    });
    this.imageStr = 'http://localhost:8080'
  }

  likePost(postId: number): void {
  /*  this.postService.addLike(postId).subscribe(() => {
      this.ngOnInit(); // Refresh posts to update likes count*/
    // });
  }
}
