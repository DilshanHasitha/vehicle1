import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExpenseAccountComponent } from '../list/expense-account.component';
import { ExpenseAccountDetailComponent } from '../detail/expense-account-detail.component';
import { ExpenseAccountUpdateComponent } from '../update/expense-account-update.component';
import { ExpenseAccountRoutingResolveService } from './expense-account-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const expenseAccountRoute: Routes = [
  {
    path: '',
    component: ExpenseAccountComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExpenseAccountDetailComponent,
    resolve: {
      expenseAccount: ExpenseAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExpenseAccountUpdateComponent,
    resolve: {
      expenseAccount: ExpenseAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExpenseAccountUpdateComponent,
    resolve: {
      expenseAccount: ExpenseAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(expenseAccountRoute)],
  exports: [RouterModule],
})
export class ExpenseAccountRoutingModule {}
