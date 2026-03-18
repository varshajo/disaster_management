import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { SignupComponent } from './components/signup/signup';
import { LandingComponent } from './components/landing/landing';
import { DashboardLayoutComponent } from './components/dashboard-layout/dashboard-layout';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard';
import { CitizenDashboardComponent } from './components/citizen-dashboard/citizen-dashboard';
import { ResponderDashboardComponent } from './components/responder-dashboard/responder-dashboard';

export const routes: Routes = [
  { path: '', redirectTo: '/landing', pathMatch: 'full' },
  { path: 'landing', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { 
    path: 'dashboard', 
    component: DashboardLayoutComponent,
    children: [
      { path: '', redirectTo: '/dashboard/home', pathMatch: 'full' },
      { path: 'home', component: AdminDashboardComponent }, // Default for now
      { path: 'admin', component: AdminDashboardComponent },
      { path: 'citizen', component: CitizenDashboardComponent },
      { path: 'responder', component: ResponderDashboardComponent }
    ]
  },
  { path: '**', redirectTo: '/landing' }
];
