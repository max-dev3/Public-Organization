import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { PostService, Post } from "../services/post.service";
import {Router} from "@angular/router";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent {
  createPostForm: FormGroup;
  submitted = false;
  imageFile: File | null = null;


  constructor(private formBuilder: FormBuilder, private userService: AuthService, private router: Router, private postService: PostService) {
    this.createPostForm = this.formBuilder.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      image: ['', Validators.required],
    });
  }

  ngOnInit() { }

  get f() { return this.createPostForm.controls; }

  onFileChange(event: Event) {
    const target = event.target as HTMLInputElement;
    const files = target.files as FileList;
    if (files.length > 0) {
      this.imageFile = files[0];
      this.createPostForm.patchValue({image: files[0].name});
    }
  }
  onSubmit() {
    this.submitted = true;

    if (this.createPostForm.invalid) {
      return;
    }

    const post: { title: any; content: any,user: { id: number } } = {
      title: this.createPostForm.get('title')?.value,
      content: this.createPostForm.get('content')?.value,
      user: { id: this.userService.getUser().id }
    };

    if (this.imageFile) {
      this.postService.createPost(post, this.imageFile).subscribe(
        data => {
          this.router.navigate(['/profile']);
          },
        error => {
          // handle error
          console.error(error);

        }
      );
    }
  }
}
