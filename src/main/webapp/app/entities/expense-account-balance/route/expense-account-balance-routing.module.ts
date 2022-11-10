import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExpenseAccountBalanceComponent } from '../list/expense-account-balance.component';
import { ExpenseAccountBalanceDetailComponent } from '../detail/expense-account-balance-detail.component';
import { ExpenseAccountBalanceUpdateComponent } from '../update/expense-account-balance-update.component';
import { ExpenseAccountBalanceRoutingResolveService } from './expense-account-balance-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const expenseAccountBalanceRoute: Routes = [
  {
    path: '',
    component: ExpenseAccountBalanceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExpenseAccountBalanceDetailComponent,
    resolve: {
      expenseAccountBalance: ExpenseAccountBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExpenseAccountBalanceUpdateComponent,
    resolve: {
      expenseAccountBalance: ExpenseAccountBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExpenseAccountBalanceUpdateComponent,
    resolve: {
      expenseAccountBalance: ExpenseAccountBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(expenseAccountBalanceRoute)],
  exports: [RouterModule],
})
export class ExpenseAccountBalanceRoutingModule {}
