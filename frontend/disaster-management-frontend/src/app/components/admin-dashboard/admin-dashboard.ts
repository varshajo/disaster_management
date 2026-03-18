import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { DisasterService } from '../../services/disaster.service';
import { DisasterEvent, RescueTask } from '../../models/disaster.models';
import { Map } from '../map/map';
import { ChartComponent } from '../chart/chart';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, DatePipe, Map, ChartComponent],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css'
})
export class AdminDashboardComponent implements OnInit {
  pendingDisasters: DisasterEvent[] = [];
  activeDisasters: DisasterEvent[] = [];
  rescueTasks: RescueTask[] = [];
  stats = { pending: 0, verified: 0 };
  analytics = { totalDisasters: 0, criticalAlerts: 0, activeRescueTasks: 0, totalUsers: 0 };
  typeDistribution: any = {};
  monthlyTrends: any = {};
  loading = true;

  constructor(private disasterService: DisasterService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    
    // Load all admin data
    this.disasterService.getPendingDisasters().subscribe({
      next: (disasters: DisasterEvent[]) => {
        this.pendingDisasters = disasters;
      },
      error: (error: any) => console.error('Error loading pending disasters:', error)
    });

    this.disasterService.getActiveDisasters().subscribe({
      next: (disasters: DisasterEvent[]) => {
        this.activeDisasters = disasters;
      },
      error: (error: any) => console.error('Error loading active disasters:', error)
    });

    this.disasterService.getAdminStats().subscribe({
      next: (stats: { pending: number; verified: number }) => {
        this.stats = stats;
      },
      error: (error: any) => console.error('Error loading stats:', error)
    });

    this.disasterService.getAllRescueTasks().subscribe({
      next: (tasks: RescueTask[]) => {
        this.rescueTasks = tasks;
        this.loadAnalytics();
      },
      error: (error: any) => {
        console.error('Error loading rescue tasks:', error);
        this.loading = false;
      }
    });
  }

  approveDisaster(id: number): void {
    this.disasterService.approveDisaster(id).subscribe({
      next: (disaster: DisasterEvent) => {
        this.loadDashboardData(); // Refresh data
      },
      error: (error: any) => console.error('Error approving disaster:', error)
    });
  }

  rejectDisaster(id: number): void {
    this.disasterService.rejectDisaster(id).subscribe({
      next: (disaster: DisasterEvent) => {
        this.loadDashboardData(); // Refresh data
      },
      error: (error: any) => console.error('Error rejecting disaster:', error)
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

  getStatusBadgeClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'PENDING': return 'bg-yellow-100 text-yellow-800';
      case 'APPROVED': return 'bg-green-100 text-green-800';
      case 'REJECTED': return 'bg-red-100 text-red-800';
      case 'IN_PROGRESS': return 'bg-blue-100 text-blue-800';
      case 'COMPLETED': return 'bg-green-100 text-green-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  loadAnalytics(): void {
    this.disasterService.getAnalyticsSummary().subscribe({
      next: (analytics: any) => {
        this.analytics = analytics;
      },
      error: (error: any) => console.error('Error loading analytics:', error)
    });

    this.disasterService.getDisasterTypeDistribution().subscribe({
      next: (distribution: any) => {
        this.typeDistribution = distribution;
      },
      error: (error: any) => console.error('Error loading type distribution:', error)
    });

    this.disasterService.getMonthlyTrends().subscribe({
      next: (trends: any) => {
        this.monthlyTrends = trends;
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error loading monthly trends:', error);
        this.loading = false;
      }
    });
  }

  getTypeDistributionData(): any {
    const labels = Object.keys(this.typeDistribution);
    const data = Object.values(this.typeDistribution);
    return {
      labels,
      datasets: [{
        data,
        backgroundColor: [
          '#FF6384',
          '#36A2EB',
          '#FFCE56',
          '#4BC0C0',
          '#9966FF'
        ]
      }]
    };
  }

  getMonthlyTrendsData(): any {
    return {
      labels: ['Last Month', 'This Month'],
      datasets: [{
        label: 'Disaster Reports',
        data: [this.monthlyTrends.lastMonth || 0, this.monthlyTrends.thisMonth || 0],
        backgroundColor: '#36A2EB'
      }]
    };
  }
}
