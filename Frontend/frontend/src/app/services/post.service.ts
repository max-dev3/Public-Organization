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
  private apiUrl = '/api/posts';

  constructor(private http: HttpClient) {}

  getAllPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.apiUrl);
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
}
