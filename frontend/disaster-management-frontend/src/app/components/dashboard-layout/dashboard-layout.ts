import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard-layout',
  standalone: true,
  imports: [CommonModule, DatePipe, RouterOutlet],
  templateUrl: './dashboard-layout.html',
  styleUrl: './dashboard-layout.css'
})
export class DashboardLayoutComponent {
  currentUser: any;
  sidebarOpen = false;
  currentDate: Date = new Date();

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.currentUser = this.authService.getCurrentUser();
  }

  logout(): void {
    this.authService.removeToken();
    this.router.navigate(['/login']);
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
  }

  get userRole(): string {
    return this.currentUser?.roles?.[0] || 'ROLE_CITIZEN';
  }

  get isAdmin(): boolean {
    return this.userRole === 'ROLE_ADMIN';
  }

  get isResponder(): boolean {
    return this.userRole === 'ROLE_RESPONDER';
  }

  get isCitizen(): boolean {
    return this.userRole === 'ROLE_CITIZEN';
  }
}
