export interface DisasterEvent {
  id: number;
  disasterType: string;
  location: string;
  severity: string;
  description: string;
  reportedBy: string;
  reportedAt: string;
  status: string;
  approvedBy?: string;
  approvedAt?: string;
}

export interface Alert {
  id: number;
  disasterId: number;
  region: string;
  message: string;
  severity: string;
  createdAt: string;
  isActive: boolean;
}

export interface RescueTask {
  id: number;
  disasterId: number;
  responderId: number;
  description: string;
  status: string;
  assignedAt: string;
  completedAt?: string;
  disaster?: DisasterEvent;
}

export enum DisasterType {
  EARTHQUAKE = 'EARTHQUAKE',
  FLOOD = 'FLOOD',
  FIRE = 'FIRE',
  HURRICANE = 'HURRICANE',
  TORNADO = 'TORNADO',
  LANDSLIDE = 'LANDSLIDE'
}

export enum DisasterStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  RESOLVED = 'RESOLVED'
}

export enum AlertSeverity {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL'
}

export enum RescueTaskStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED'
}
