import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/users';
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());

  constructor(private http: HttpClient) {}

  hasToken(): boolean {
    return !!localStorage.getItem('loggedIn');
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, user);
  }

  login(credentials: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials)
      .pipe(

        tap(response => {

          localStorage.setItem('user', JSON.stringify(response));

          this.setLoggedInStatus(true);
        })
      );
  }


  getUser(): any {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }
  deleteAccount(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  setLoggedInStatus(status: boolean) {
    this.loggedIn.next(status);
    if (status === true) {
      localStorage.setItem('loggedIn', 'true');
    } else {
      localStorage.removeItem('loggedIn');
    }
  }

  logout(): void {
    localStorage.removeItem('loggedIn');
    this.loggedIn.next(false);
  }
}
