import {Component, OnInit} from '@angular/core';
import { AuthService } from '../services/auth.service';
import {Router} from "@angular/router";
import {Post, PostService} from "../services/post.service";


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: any;
  isDialogVisible = false;
  userPosts: Post[] = [];
  likedPosts: Post[] = [];



  constructor(private authService: AuthService, private postService: PostService, private router: Router) { }

  ngOnInit(): void {
    this.user = this.authService.getUser();
    this.postService.getUserPosts(this.user.id).subscribe(
      posts => this.userPosts = posts,
      error => console.error('Failed to fetch user posts', error)
    );
  }

  showDialog() {
    this.isDialogVisible = true;
  }
  showingOwnProjects = true;

  showOwnProjects() {
    this.showingOwnProjects = true;
  }



  hideDialog() {
    this.isDialogVisible = false;
  }
  showLikedProjects() {
    this.showingOwnProjects = false;
    this.postService.getLikedPosts(this.user.id).subscribe(
      posts => this.likedPosts = posts,
      error => console.error('Failed to fetch liked posts', error)
    );
  }
  deleteAccount() {
    this.authService.deleteAccount(this.user.id).subscribe(
      response => {
        console.log('Account deleted successfully', response);
        this.authService.logout();
        this.router.navigate(['/']);
      },
      error => {
        console.log('Account deletion failed', error);
      }
    );
  }
}

