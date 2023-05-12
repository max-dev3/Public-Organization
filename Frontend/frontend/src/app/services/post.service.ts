import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Post {
  id: number;
  title: string;
  content: string;
  imageUrl: string;
  user: User;
  createdAt: Date;
  updatedAt: Date;
  likes: Like[];
}

export interface User {
  id: number;
  lastName: string;
  firstName: string;
  // Додайте інші поля моделі User, які вам потрібні
}

export interface Like {
  id: number;
  // Додайте інші поля моделі Like, які вам потрібні
}

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) {}

  getAllPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.apiUrl);
  }
  getUserPosts(userId: number): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.apiUrl}/user/${userId}`);
  }
  getPostById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.apiUrl}/${id}`);
  }

  createPost(post: Post): Observable<Post> {
    return this.http.post<Post>(this.apiUrl, post);
  }

  updatePost(id: number, updatedPost: Post): Observable<Post> {
    return this.http.put<Post>(`${this.apiUrl}/${id}`, updatedPost);
  }

  deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /*approvePost(postId: number): Observable<Post> {
    return this.http.put<Post>(`${this.apiUrl}/${postId}/approve`);
  }

  rejectPost(postId: number): Observable<Post> {
    return this.http.put<Post>(`${this.apiUrl}/${postId}/reject`);
  }

  addLike(postId: number): Observable<Like> {
    return this.http.post<Like>(`${this.apiUrl}/${postId}/likes`);
  }

  removeLike(postId: number, likeId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${postId}/likes/${likeId}`);
  }*/
}
