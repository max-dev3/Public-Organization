import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

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
    return this.http.post(`${this.apiUrl}/login`, credentials);
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
