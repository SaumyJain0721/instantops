export type BookingStatus = 'PENDING'|'ASSIGNED'|'ON_THE_WAY'|'IN_PROGRESS'|'COMPLETED'|'CANCELLED'
export type MechanicStatus = 'AVAILABLE'|'ON_DUTY'|'BUSY'|'OFF_DUTY'
export interface ApiResponse<T>{success:boolean;message:string;data:T;timestamp:string}
export interface PageResponse<T>{content:T[];page:number;size:number;totalElements:number;totalPages:number;first:boolean;last:boolean}
export interface CustomerSummary{id:number;name:string;email:string;phone:string;address:string;vehicleCount:number;bookingCount:number;createdAt:string}
export interface CustomerDetail extends CustomerSummary{vehicles:VehicleSummary[];updatedAt:string}
export interface VehicleSummary{id:number;make:string;model:string;year:number;licensePlate:string;color:string;fuelType:string}
export interface MechanicBookingSummary{id:number;bookingNumber:string;customerName:string;vehicleInfo:string;serviceName:string;status:BookingStatus;scheduledAt:string}
export interface MechanicSummary{id:number;name:string;email:string;phone:string;specialization:string;status:MechanicStatus;avatarUrl:string|null;activeBookingsCount:number;jobsCompleted:number;currentBooking:MechanicBookingSummary|null;lastBooking:MechanicBookingSummary|null;createdAt:string}
export type MechanicDetail = MechanicSummary
export interface ServiceSummary{id:number;name:string;description:string;price:number;estimatedDurationMinutes:number;createdAt:string}
export interface BookingSummary{id:number;bookingNumber:string;customerId:number;customerName:string;customerPhone:string;customerEmail:string;vehicleId:number;vehicleInfo:string;licensePlate:string;serviceId:number;serviceName:string;estimatedDurationMinutes:number;mechanicId:number|null;mechanicName:string|null;mechanicSpecialization:string|null;status:BookingStatus;totalAmount:number;scheduledAt:string;completedAt:string|null;notes:string|null;createdAt:string;updatedAt:string}
export interface BookingDetail{id:number;bookingNumber:string;customer:CustomerSummary;vehicle:VehicleSummary;service:ServiceSummary;mechanic:MechanicSummary|null;status:BookingStatus;totalAmount:number;scheduledAt:string;completedAt:string|null;notes:string|null;createdAt:string;updatedAt:string}
export interface DashboardKpis{totalBookings:number;todayBookings:number;completedBookings:number;pendingBookings:number;cancelledBookings:number;totalRevenue:number;activeMechanics:number;newCustomers:number}
export interface OperationsPulse{pending:number;assigned:number;onTheWay:number;inProgress:number;completed:number;cancelled:number}
export interface TimeSeriesPoint{date:string;count:number;revenue:number}
export interface StatusDistribution{status:BookingStatus;count:number;percentage:number}
export interface ServiceBreakdownItem{serviceName:string;bookingCount:number;revenue:number}
export interface RecentActivityItem extends BookingSummary{}
export interface DashboardData{summary:DashboardKpis;operationsPulse:OperationsPulse;bookingsOverTime:TimeSeriesPoint[];revenueOverTime:TimeSeriesPoint[];statusDistribution:StatusDistribution[];serviceBreakdown:ServiceBreakdownItem[];recentActivity:RecentActivityItem[]}
export interface BookingStatusChangedEvent{bookingId:number;bookingNumber:string;previousStatus:BookingStatus;newStatus:BookingStatus;customerName:string;customerPhone:string;vehicleInfo:string;licensePlate:string;serviceName:string;mechanicName:string|null;totalAmount:number;notes:string|null;eventType:string;timestamp:string}
export interface BookingFilters{page?:number;size?:number;search?:string;status?:BookingStatus|'';mechanicId?:number|'';serviceId?:number|'';sortBy?:string;sortDir?:'asc'|'desc'}
