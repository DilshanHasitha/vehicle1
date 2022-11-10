import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeTypeComponent } from '../list/employee-type.component';
import { EmployeeTypeDetailComponent } from '../detail/employee-type-detail.component';
import { EmployeeTypeUpdateComponent } from '../update/employee-type-update.component';
import { EmployeeTypeRoutingResolveService } from './employee-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employeeTypeRoute: Routes = [
  {
    path: '',
    component: EmployeeTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeTypeDetailComponent,
    resolve: {
      employeeType: EmployeeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeTypeUpdateComponent,
    resolve: {
      employeeType: EmployeeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeTypeUpdateComponent,
    resolve: {
      employeeType: EmployeeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeTypeRoute)],
  exports: [RouterModule],
})
export class EmployeeTypeRoutingModule {}
