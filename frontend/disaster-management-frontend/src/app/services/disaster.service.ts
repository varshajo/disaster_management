import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DisasterEvent, Alert, RescueTask } from '../models/disaster.models';
import { MessageResponse } from '../models/auth.models';

@Injectable({
  providedIn: 'root'
})
export class DisasterService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  // Public endpoints
  getActiveDisasters(disasterType?: string, location?: string): Observable<DisasterEvent[]> {
    let params = '';
    if (disasterType) params += `?disasterType=${disasterType}`;
    if (location) params += params ? `&location=${location}` : `?location=${location}`;
    return this.http.get<DisasterEvent[]>(`${this.apiUrl}/disasters/active${params}`);
  }

  getAlertsByRegion(region: string): Observable<Alert[]> {
    return this.http.get<Alert[]>(`${this.apiUrl}/disasters/region/${region}`);
  }

  // Admin endpoints
  getPendingDisasters(): Observable<DisasterEvent[]> {
    return this.http.get<DisasterEvent[]>(`${this.apiUrl}/admin/disasters/pending`);
  }

  approveDisaster(id: number): Observable<DisasterEvent> {
    return this.http.put<DisasterEvent>(`${this.apiUrl}/admin/disasters/${id}/approve`, {});
  }

  rejectDisaster(id: number): Observable<DisasterEvent> {
    return this.http.put<DisasterEvent>(`${this.apiUrl}/admin/disasters/${id}/reject`, {});
  }

  getAdminStats(): Observable<{ pending: number; verified: number }> {
    return this.http.get<{ pending: number; verified: number }>(`${this.apiUrl}/admin/disasters/stats`);
  }

  getAnalyticsSummary(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/admin/analytics/summary`);
  }

  getDisasterTypeDistribution(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/admin/analytics/type-distribution`);
  }

  getMonthlyTrends(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/admin/analytics/monthly-trends`);
  }

  deleteDisaster(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/admin/disasters/${id}`);
  }

  // Rescue Admin endpoints
  assignTask(disasterId: number, responderId: number, description: string): Observable<RescueTask> {
    return this.http.post<RescueTask>(`${this.apiUrl}/admin/rescue/assign`, {
      disasterId,
      responderId,
      description
    });
  }

  getAllRescueTasks(): Observable<RescueTask[]> {
    return this.http.get<RescueTask[]>(`${this.apiUrl}/admin/rescue/all`);
  }

  // Citizen endpoints
  reportDisaster(disaster: { disasterType: string; location: string; severity: string; description: string }): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.apiUrl}/citizen/report`, disaster);
  }

  getMyAlerts(): Observable<Alert[]> {
    return this.http.get<Alert[]>(`${this.apiUrl}/citizen/alerts`);
  }

  // Responder endpoints
  getTasksByResponderId(responderId: number): Observable<RescueTask[]> {
    return this.http.get<RescueTask[]>(`${this.apiUrl}/responder/tasks/${responderId}`);
  }

  updateTaskStatus(taskId: number, status: string): Observable<RescueTask> {
    return this.http.put<RescueTask>(`${this.apiUrl}/responder/tasks/${taskId}/status`, { status });
  }

  acknowledgeAlert(alertId: number): Observable<Alert> {
    return this.http.put<Alert>(`${this.apiUrl}/responder/alerts/${alertId}/acknowledge`, {});
  }
}
