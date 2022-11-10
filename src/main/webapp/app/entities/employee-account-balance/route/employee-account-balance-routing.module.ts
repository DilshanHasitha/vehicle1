import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeAccountBalanceComponent } from '../list/employee-account-balance.component';
import { EmployeeAccountBalanceDetailComponent } from '../detail/employee-account-balance-detail.component';
import { EmployeeAccountBalanceUpdateComponent } from '../update/employee-account-balance-update.component';
import { EmployeeAccountBalanceRoutingResolveService } from './employee-account-balance-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employeeAccountBalanceRoute: Routes = [
  {
    path: '',
    component: EmployeeAccountBalanceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeAccountBalanceDetailComponent,
    resolve: {
      employeeAccountBalance: EmployeeAccountBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeAccountBalanceUpdateComponent,
    resolve: {
      employeeAccountBalance: EmployeeAccountBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeAccountBalanceUpdateComponent,
    resolve: {
      employeeAccountBalance: EmployeeAccountBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeAccountBalanceRoute)],
  exports: [RouterModule],
})
export class EmployeeAccountBalanceRoutingModule {}
