import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExpenseTypeComponent } from '../list/expense-type.component';
import { ExpenseTypeDetailComponent } from '../detail/expense-type-detail.component';
import { ExpenseTypeUpdateComponent } from '../update/expense-type-update.component';
import { ExpenseTypeRoutingResolveService } from './expense-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const expenseTypeRoute: Routes = [
  {
    path: '',
    component: ExpenseTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExpenseTypeDetailComponent,
    resolve: {
      expenseType: ExpenseTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExpenseTypeUpdateComponent,
    resolve: {
      expenseType: ExpenseTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExpenseTypeUpdateComponent,
    resolve: {
      expenseType: ExpenseTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(expenseTypeRoute)],
  exports: [RouterModule],
})
export class ExpenseTypeRoutingModule {}
