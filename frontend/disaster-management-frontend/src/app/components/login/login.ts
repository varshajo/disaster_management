import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/auth.models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const loginRequest: LoginRequest = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    };

    this.authService.login(loginRequest).subscribe({
      next: (response) => {
        this.authService.setToken(response.token);
        this.authService.setCurrentUser({
          id: response.id,
          email: response.email,
          name: response.name,
          roles: response.roles
        });
        
        // Redirect to role-specific dashboard
        const userRole = response.roles[0];
        let dashboardRoute = '/dashboard/home';
        
        if (userRole === 'ROLE_ADMIN') {
          dashboardRoute = '/dashboard/admin';
        } else if (userRole === 'ROLE_CITIZEN') {
          dashboardRoute = '/dashboard/citizen';
        } else if (userRole === 'ROLE_RESPONDER') {
          dashboardRoute = '/dashboard/responder';
        }
        
        this.router.navigate([dashboardRoute]);
      },
      error: (error) => {
        this.errorMessage = 'Login failed. Please check your credentials.';
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
}
