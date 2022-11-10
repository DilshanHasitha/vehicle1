import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeAccountComponent } from '../list/employee-account.component';
import { EmployeeAccountDetailComponent } from '../detail/employee-account-detail.component';
import { EmployeeAccountUpdateComponent } from '../update/employee-account-update.component';
import { EmployeeAccountRoutingResolveService } from './employee-account-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employeeAccountRoute: Routes = [
  {
    path: '',
    component: EmployeeAccountComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeAccountDetailComponent,
    resolve: {
      employeeAccount: EmployeeAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeAccountUpdateComponent,
    resolve: {
      employeeAccount: EmployeeAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeAccountUpdateComponent,
    resolve: {
      employeeAccount: EmployeeAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeAccountRoute)],
  exports: [RouterModule],
})
export class EmployeeAccountRoutingModule {}
