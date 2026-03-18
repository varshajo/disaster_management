import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { DisasterService } from '../../services/disaster.service';
import { RescueTask, RescueTaskStatus } from '../../models/disaster.models';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-responder-dashboard',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './responder-dashboard.html',
  styleUrl: './responder-dashboard.css'
})
export class ResponderDashboardComponent implements OnInit {
  myTasks: RescueTask[] = [];
  currentUser: any;
  responderId: number = 0;
  loading = true;
  updatingTaskId: number | null = null;

  constructor(
    private disasterService: DisasterService,
    private authService: AuthService
  ) {
    this.currentUser = this.authService.getCurrentUser();
    this.responderId = this.currentUser?.id || 1; // Default to 1 for demo
  }

  ngOnInit(): void {
    this.loadMyTasks();
  }

  loadMyTasks(): void {
    this.loading = true;
    this.disasterService.getTasksByResponderId(this.responderId).subscribe({
      next: (tasks: RescueTask[]) => {
        this.myTasks = tasks;
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error loading tasks:', error);
        this.loading = false;
      }
    });
  }

  updateTaskStatus(taskId: number, newStatus: string): void {
    this.updatingTaskId = taskId;
    this.disasterService.updateTaskStatus(taskId, newStatus).subscribe({
      next: (task: RescueTask) => {
        this.loadMyTasks(); // Refresh tasks
        this.updatingTaskId = null;
      },
      error: (error: any) => {
        console.error('Error updating task status:', error);
        this.updatingTaskId = null;
      }
    });
  }

  getStatusBadgeClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'PENDING': return 'bg-yellow-100 text-yellow-800';
      case 'IN_PROGRESS': return 'bg-blue-100 text-blue-800';
      case 'COMPLETED': return 'bg-green-100 text-green-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  getTaskCountByStatus(status: string): number {
    return this.myTasks.filter(task => task.status === status).length;
  }

  getNextStatus(currentStatus: string): string[] {
    switch (currentStatus?.toUpperCase()) {
      case 'PENDING': return ['IN_PROGRESS'];
      case 'IN_PROGRESS': return ['COMPLETED'];
      case 'COMPLETED': return [];
      default: return [];
    }
  }

  canUpdateStatus(status: string): boolean {
    return this.getNextStatus(status).length > 0;
  }
}
