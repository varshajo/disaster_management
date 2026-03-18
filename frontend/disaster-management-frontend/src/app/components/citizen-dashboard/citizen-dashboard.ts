import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { DisasterService } from '../../services/disaster.service';
import { Alert, DisasterEvent, DisasterType } from '../../models/disaster.models';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-citizen-dashboard',
  standalone: true,
  imports: [CommonModule, DatePipe, ReactiveFormsModule],
  templateUrl: './citizen-dashboard.html',
  styleUrl: './citizen-dashboard.css'
})
export class CitizenDashboardComponent implements OnInit {
  alerts: Alert[] = [];
  activeDisasters: DisasterEvent[] = [];
  currentUser: any;
  userRegion: string = '';
  loading = true;
  showReportForm = false;
  reportForm: FormGroup;
  submitting = false;

  constructor(
    private disasterService: DisasterService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.currentUser = this.authService.getCurrentUser();
    this.userRegion = this.currentUser?.region || 'Default Region';
    
    this.reportForm = this.fb.group({
      disasterType: ['', Validators.required],
      location: [this.userRegion, Validators.required],
      severity: ['', Validators.required],
      description: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  ngOnInit(): void {
    this.loadCitizenData();
  }

  loadCitizenData(): void {
    this.loading = true;
    
    // Load alerts for user's region
    this.disasterService.getMyAlerts().subscribe({
      next: (alerts) => {
        this.alerts = alerts;
      },
      error: (error) => console.error('Error loading alerts:', error)
    });

    // Load active disasters
    this.disasterService.getActiveDisasters().subscribe({
      next: (disasters) => {
        this.activeDisasters = disasters.filter(d => 
          d.location.toLowerCase().includes(this.userRegion.toLowerCase())
        );
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading disasters:', error);
        this.loading = false;
      }
    });
  }

  toggleReportForm(): void {
    this.showReportForm = !this.showReportForm;
    if (!this.showReportForm) {
      this.reportForm.reset();
      this.reportForm.patchValue({ location: this.userRegion });
    }
  }

  submitReport(): void {
    if (this.reportForm.invalid) return;
    
    this.submitting = true;
    const reportData = this.reportForm.value;
    
    this.disasterService.reportDisaster(reportData).subscribe({
      next: () => {
        this.submitting = false;
        this.showReportForm = false;
        this.reportForm.reset();
        this.reportForm.patchValue({ location: this.userRegion });
        this.loadCitizenData(); // Refresh data
      },
      error: (error) => {
        console.error('Error reporting disaster:', error);
        this.submitting = false;
      }
    });
  }

  getSeverityBadgeClass(severity: string): string {
    switch (severity?.toUpperCase()) {
      case 'LOW': return 'bg-green-100 text-green-800';
      case 'MEDIUM': return 'bg-yellow-100 text-yellow-800';
      case 'HIGH': return 'bg-orange-100 text-orange-800';
      case 'CRITICAL': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  getAlertSeverityClass(severity: string): string {
    switch (severity?.toUpperCase()) {
      case 'LOW': return 'border-l-green-500 bg-green-50';
      case 'MEDIUM': return 'border-l-yellow-500 bg-yellow-50';
      case 'HIGH': return 'border-l-orange-500 bg-orange-50';
      case 'CRITICAL': return 'border-l-red-500 bg-red-50';
      default: return 'border-l-gray-500 bg-gray-50';
    }
  }
}
